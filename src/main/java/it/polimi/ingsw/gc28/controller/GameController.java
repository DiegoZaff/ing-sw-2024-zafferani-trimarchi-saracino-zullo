package it.polimi.ingsw.gc28.controller;
import it.polimi.ingsw.gc28.model.errors.types.*;
import it.polimi.ingsw.gc28.model.utils.JoinInfo;
import it.polimi.ingsw.gc28.network.messages.client.MessageC2S;
import it.polimi.ingsw.gc28.network.persistence.BackupManager;
import it.polimi.ingsw.gc28.view.GameRepresentation;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.cards.CardObjective;
import it.polimi.ingsw.gc28.model.cards.CardResource;
import it.polimi.ingsw.gc28.model.cards.CardsManager;
import it.polimi.ingsw.gc28.model.chat.ChatMessage;
import it.polimi.ingsw.gc28.model.errors.PlayerActionError;
import it.polimi.ingsw.gc28.network.messages.server.*;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GameController {
    final Game gameModel;

    private final Map<String, VirtualView> clients;

    private final BlockingQueue<MessageC2S> messageQueue;

    public GameController(Game gameModel) {
        this.gameModel = gameModel;
        this.clients = new HashMap<>();
        messageQueue = new LinkedBlockingQueue<>();
        this.processIncomingMessages();
    }

    private void processIncomingMessages() {
        new Thread(() -> {
            while (true) {
                try {
                    MessageC2S message = messageQueue.take(); // Blocking call
                    try{
                        message.execute(this);
                    }catch (RemoteException e){
                        System.err.println("Remote Exception while executing a message!");
                        System.err.println(e.getMessage());
                    } catch (PlayerActionError e) {
                        System.err.println("Error should be already managed!");
                    }
                } catch (InterruptedException e) {
                    System.err.println("Thread was interrupted while taking a message!");
                    System.err.println(e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public void addMessageToQueue(MessageC2S message){
        try {
            messageQueue.put(message);
        } catch (InterruptedException e) {
            System.err.println("Thread was interrupted while inserting a message!");
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean addPlayerToGame(String name, VirtualView client) throws RemoteException {

        synchronized (gameModel){
            try{
                this.gameModel.addPlayerToGame(name);
                this.clients.put(name, client);
            }catch (PlayerActionError e){
                // notify error to player
                MsgReportError message = new MsgReportError(e.getError());
                client.sendMessage(message);
                return false;
            }

            return true;
        }
    }

    /**
     * This is called after a player has joined. If the game has started this method notifies all client.
     */
    public void hasGameStarted() throws RemoteException {
        synchronized (gameModel){
            if (gameModel.getHasGameStarted()){
                GameRepresentation representation = getGameRepresentation();
                for(Map.Entry<String, VirtualView> entry : clients.entrySet()){
                    VirtualView cli = entry.getValue();
                    MsgOnGameStarted m = new MsgOnGameStarted(representation);
                    cli.sendMessage(m);
                }
            }
        }
    }

    /**
     * This is called after a player has joined. If the game has started this method notifies all client.
     */
    public void hasGameRestarted() throws RemoteException {
        synchronized (gameModel){
            if (gameModel.isEveryoneReconnected()){
                GameRepresentation representation = getGameRepresentation();
                for(Map.Entry<String, VirtualView> entry : clients.entrySet()){
                    VirtualView cli = entry.getValue();
                    MsgOnGameRestarted m = new MsgOnGameRestarted(representation);
                    cli.sendMessage(m);
                }
            }
        }
    }


    /**
     * Overloading, in case we have already calculated Player.
     */
//    public Optional<ArrayList<CardObjective>> getPersonalObjectives(Player player){
//        synchronized (gameModel) {
//            return player.getObjectivesToChoose();
//        }
//    }
//
//    public ArrayList<CardResource> getPlayerHand(Player player){
//        synchronized (gameModel) {
//            return player.gethand();
//        }
//    }


    public void chooseObjectivePersonal(String name, String cardId) throws RemoteException {

        Optional<CardObjective> chosen = CardsManager.getInstance().getCardObjectiveFromId(cardId);

        if(chosen.isEmpty()){
            notifyError(name, new NoSuchCardId(cardId), "ChooseObjective");
            return;
        }

        synchronized (gameModel){

            try{
                gameModel.chooseObjective(name, chosen.get());

                notifyObjChosen(name);

                // back up game
                backUpGame(gameModel);
            }catch (PlayerActionError e){
                notifyError(name, e, "ChooseObjective");
            }
        }
    }

    public void drawCard(String name, boolean fromGoldDeck) throws RemoteException {
        synchronized (gameModel){
            try{
                CardResource card = gameModel.drawGameCard(name, fromGoldDeck);

                notifyOfCardDrawn(name, card, fromGoldDeck);

                // back up game
                backUpGame(gameModel);
            }catch (PlayerActionError e){
                notifyError(name, e, "DrawCardFromDeck");
            }
        }
    }

    public void drawCard(String playerName, String cardId) throws RemoteException {
        Optional<? extends CardResource> cardToDraw = CardsManager.getInstance().getCardResourceFromId(cardId);

        if(cardToDraw.isEmpty()){
            notifyError(playerName, new NoSuchCardId(cardId), "DrawCardFromCardId");
            return;
        }

        synchronized (gameModel){
            try {
                gameModel.drawGameCard(playerName, cardToDraw.get());

                notifyOfCardDrawn(playerName, cardToDraw.get());

                // back up game
                backUpGame(gameModel);
            } catch (PlayerActionError e) {
                notifyError(playerName, e, "DrawCardFromCardId");
            }
        }
    }


    public void playCard(String playerName, String cardId, boolean isFront, Coordinate coordinate) throws RemoteException {
        Optional<? extends CardGame> cardToPlay = CardsManager.getInstance().getCardGameFromId(cardId);

        if(cardToPlay.isEmpty()){
            notifyError(playerName, new NoSuchCardId(cardId), "PlayCard");
            return;
        }

        synchronized (gameModel){
            try{
                gameModel.playGameCard(playerName, cardToPlay.get(), isFront, coordinate);

                Optional<Player> playerWhoPlayed = gameModel.getPlayerOfName(playerName);

                if(playerWhoPlayed.isEmpty()){
                    // TODO: handle this... should not happen
                    throw new RuntimeException("Something went seriously wrong!");
                }

                notifyOfCardPlayed(playerName, cardToPlay.get().getId());

                // back up game
                backUpGame(gameModel);

            }catch (PlayerActionError e){
                MsgReportError message = new MsgReportError(e.getError());
                try {
                    clients.get(playerName).sendMessage(message);
                    return;
                } catch (RemoteException ex) {
                    System.err.println("Could not notify client! Maybe disconnected?");
                    System.err.println(e.getMessage());
                    throw new RuntimeException(ex);
                }
            }

        }
    }

    /**
     * This method is for sending chat messages
     */
    public void sendMessage(ChatMessage chatMessage) throws RemoteException {
        synchronized (gameModel) {
            if(chatMessage.getReceiver().equals("all")){
                gameModel.sendMessage(chatMessage);
                notifyChatMessage();
                return;
            }

            Optional<Player> receiver = gameModel.getPlayerOfName(chatMessage.getReceiver());
            if (receiver.isEmpty()){
                notifyError(chatMessage.getSender(), new InvalidReceiverName(), "choose a correct player name to send the message");
                return;
            }
            gameModel.sendMessage(chatMessage);
        }
        notifyChatMessage();

        // back up game
        backUpGame(gameModel);
    }

    public void chooseColor(String playerName, String color) throws RemoteException {
        synchronized (gameModel) {
            try {
                gameModel.chooseColor(playerName, color);
                notifyChooseColor(playerName);
            } catch (PlayerActionError e) {
                notifyError(playerName, e, "Error in choosing color");
            }
        }
    }


    public void waitForReconnections(){
        synchronized (gameModel){
            try {
                gameModel.setWaitForReconnections();
            } catch (UnrestorableGameError e) {
                System.err.printf("Game %s could not be restored!\n%s%n", gameModel.getGameId(), e.getMessage());
            }
        }
    }


    public boolean reconnect(String playerName, VirtualView client) throws RemoteException {
        synchronized (gameModel){
            try {
                gameModel.reconnectPlayer(playerName);
                this.clients.put(playerName, client);
                return true;
            } catch (PlayerActionError e) {
                notifyErrorSpecificClient(client, e);
            }
            return false;
        }
    }

    public void notifyOfCardDrawn(String playerName, CardResource card, Boolean fromGoldDeck) throws RemoteException {

        GameRepresentation gameRepresentation = getGameRepresentation();

        for(Map.Entry<String, VirtualView> entry : clients.entrySet()){

            VirtualView client = entry.getValue();

            MsgOnPlayerDrawnCard message = new MsgOnPlayerDrawnCard(gameRepresentation,playerName, card.getId(), fromGoldDeck);

            client.sendMessage(message);
        }
    }

    public void notifyOfCardDrawn(String playerName, CardResource card) throws RemoteException {

        GameRepresentation gameRepresentation = getGameRepresentation();

        for(Map.Entry<String, VirtualView> entry : clients.entrySet()){

            VirtualView client = entry.getValue();

            MsgOnPlayerDrawnCard message = new MsgOnPlayerDrawnCard(gameRepresentation,playerName, card.getId(), null);

            client.sendMessage(message);

        }
    }

    public void notifyOfCardPlayed(String playerWhoPlayed, String cardPlayedId) throws RemoteException {
        GameRepresentation gameRepresentation = getGameRepresentation();

        for(Map.Entry<String, VirtualView> entry : clients.entrySet()){

            VirtualView client = entry.getValue();


            MsgOnPlayerPlayedCard message = new MsgOnPlayerPlayedCard(gameRepresentation, cardPlayedId, playerWhoPlayed);
            client.sendMessage(message);
        }
    }

    public void notifyObjChosen(String playerName) throws RemoteException {

        GameRepresentation gameRepresentation = getGameRepresentation();

        for(Map.Entry<String, VirtualView> entry : clients.entrySet()){

            VirtualView client = entry.getValue();

            MsgOnPlayerChooseObjective message = new MsgOnPlayerChooseObjective(playerName, gameRepresentation);

            client.sendMessage(message);
        }
    }

    public void notifyError(String name, PlayerActionError e, String actionDetails) throws RemoteException {
        VirtualView clientOfRequest = clients.get(name);

        if(clientOfRequest == null){
            System.err.println(actionDetails + " from non existent player!");
            return;
        }

        MsgReportError message = new MsgReportError(e.getError());
        clients.get(name).sendMessage(message);
    }

    public void notifyErrorSpecificClient(VirtualView client, PlayerActionError e) throws RemoteException {
        MsgReportError message = new MsgReportError(e.getMessage());
        client.sendMessage(message);
    }

    public void notifyGameCreated(String gameId, String name, int numberOfPlayersLeftToJoin) throws RemoteException {
        VirtualView clientOfRequest = clients.get(name);

        if(clientOfRequest == null){
            System.err.println(" from non existent player!");
            return;
        }

        int nPlayers = gameModel.getNPlayers();
        MsgOnGameCreated message = new MsgOnGameCreated(gameId, name, numberOfPlayersLeftToJoin, nPlayers);

        clients.get(name).sendMessage(message);

    }

    public void notifyPlayerReconnected(String gameId, String playerName, int playersLeft) throws RemoteException {


        for(Map.Entry<String, VirtualView> entry : clients.entrySet()){

            VirtualView client = entry.getValue();

            String name = entry.getKey();


            if(name.equals(playerName)){
                MsgOnPlayerReconnected msg = new MsgOnPlayerReconnected(gameId, playerName, playersLeft);
                client.sendMessage(msg);
            }else{
                MsgOnSomeoneElseReconnected msg= new MsgOnSomeoneElseReconnected(playerName, playersLeft);
                client.sendMessage(msg);
            }

        }
    }


    public void notifyPlayerJoined(String gameId, String playerName, int playersLeft) throws RemoteException {
        int nPlayers = this.gameModel.getNPlayers();

        for(Map.Entry<String, VirtualView> entry : clients.entrySet()){

            VirtualView client = entry.getValue();

            String name = entry.getKey();


            if(name.equals(playerName)){
                MsgOnGameJoined msg = new MsgOnGameJoined(gameId, playerName, playersLeft, nPlayers );
                client.sendMessage(msg);
            }else{
                MsgOnSomeoneElseJoined msg= new MsgOnSomeoneElseJoined(gameId, playerName, playersLeft);
                client.sendMessage(msg);
            }

        }
    }

    public int getPlayersToReconnect(){
        return gameModel.getNPlayersToReconnect();
    }

    public int getPlayersToJoin(){
        return gameModel.getPlayersToJoin();
    }

    public void notifyChatMessage() throws RemoteException {
        GameRepresentation gameRepresentation = getGameRepresentation();

        for(Map.Entry<String, VirtualView> entry : clients.entrySet()){

            VirtualView client = entry.getValue();

            MsgOnChatMessage message = new MsgOnChatMessage(gameRepresentation);

            client.sendMessage(message);
        }


    }

    public void notifyChooseColor(String name) throws RemoteException {
        GameRepresentation gameRepresentation = getGameRepresentation();

        for(Map.Entry<String, VirtualView> entry : clients.entrySet()){

            VirtualView client = entry.getValue();

            MsgOnChooseColor message = new MsgOnChooseColor(name, gameRepresentation);

            client.sendMessage(message);
        }
    }



    public GameRepresentation getGameRepresentation(){
        synchronized (gameModel){return gameModel.getGameRepresentation();}
    }


    /**
     * This method starts a thread using [BackUpManager]
     * @param game the game to be back-upped
     */
    private void backUpGame(Game game){
        new BackupManager(game).start();
    }


    public Optional<JoinInfo> getJoinInfo(){
        synchronized (gameModel){return gameModel.getJoinInfo();}
    }
}
