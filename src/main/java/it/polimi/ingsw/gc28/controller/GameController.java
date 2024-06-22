package it.polimi.ingsw.gc28.controller;
import it.polimi.ingsw.gc28.model.errors.types.*;
import it.polimi.ingsw.gc28.model.utils.GameEndedNotification;
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
import java.util.stream.Collectors;

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

    /**
     * Starts a new thread to process all the messages inside the queue
     */
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


    /**
     *
     * @param message gets added to the queue
     */
    public void addMessageToQueue(MessageC2S message){
        try {
            messageQueue.put(message);
        } catch (InterruptedException e) {
            System.err.println("Thread was interrupted while inserting a message!");
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * adds a player to the game
     * @param name of the player added
     * @param client of the player added
     * @throws RemoteException
     */
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

//
//    /**
//     * Overloading, in case we have already calculated Player.
//     */
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

    /**
     * contorls the chooseObjective action
     * @param name of the player that choose the objective
     * @param cardId of the card chosen as objective
     * @throws RemoteException
     */
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

    /**
     * controls the drawCard action when the card drawn is a hidden card
     * @param name of the player that has drawn the card
     * @param fromGoldDeck boolean that tells from which deck to draw the card
     * @throws RemoteException
     */
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

    /**
     * controls the drawCard action when the card drawn is a face-up card
     * @param playerName of the player that has drawn the card
     * @param cardId of the face up card to draw
     * @throws RemoteException
     */
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

    /**
     * controls the playCard action
     * @param playerName that played the card
     * @param cardId to be played
     * @param isFront side of the card
     * @param coordinate where the card is being played
     * @throws RemoteException
     */
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
                    //TODO : handle better?
                    System.err.println("Could not notify client! Maybe disconnected?");
                    System.err.println(e.getMessage());
                    throw new RuntimeException(ex);
                }
            } catch (GameEndedNotification e) {

                notifyOfCardPlayed(playerName, cardToPlay.get().getId());

                ArrayList<String> playersWin = gameModel.getPlayers().stream().filter((Player::isWinner)).map(Player::getName).collect(Collectors.toCollection(ArrayList::new));

                notifyGameEndedWinners(playersWin);

                //delete backUp file
                deleteBackUpGame(gameModel);
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
                String sender = chatMessage.getSender();
                String receiver = chatMessage.getReceiver();
                notifyChatMessage(sender, receiver, false);
                return;
            }

            else {
                Optional<Player> receiver = gameModel.getPlayerOfName(chatMessage.getReceiver());
                if (receiver.isEmpty()){
                    notifyError(chatMessage.getSender(), new InvalidReceiverName(), "choose a correct player name to send the message");
                    return;
                }
                gameModel.sendMessage(chatMessage);
                String sender = chatMessage.getSender();
                String rec = chatMessage.getReceiver();
                notifyChatMessage(sender, rec, true);
            }
        }

        // back up game
        backUpGame(gameModel);
    }

    /**
     * contorls the chooseColor action
     * @param playerName that has chosen the color
     * @param color chosen from the player
     * @throws RemoteException
     */
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

    /**
     * controls the reconnection of a player
     * @param playerName that is reconnecting
     * @param client of the player
     * @throws RemoteException
     */
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

    /**
     * notifies the client that the hidden card has been successfully drawn
     * @param playerName to notify
     * @param card drawn
     * @param fromGoldDeck boolean that tells from which deck to draw the card
     * @throws RemoteException
     */
    public void notifyOfCardDrawn(String playerName, CardResource card, Boolean fromGoldDeck) throws RemoteException {

        GameRepresentation gameRepresentation = getGameRepresentation();

        for(Map.Entry<String, VirtualView> entry : clients.entrySet()){

            VirtualView client = entry.getValue();

            MsgOnPlayerDrawnCard message = new MsgOnPlayerDrawnCard(gameRepresentation,playerName, card.getId(), fromGoldDeck);

            client.sendMessage(message);
        }
    }

    /**
     * notifies the client that the face-up card has been successfully drawn
     * @param playerName to notigy
     * @param card draw
     * @throws RemoteException
     */
    public void notifyOfCardDrawn(String playerName, CardResource card) throws RemoteException {

        GameRepresentation gameRepresentation = getGameRepresentation();

        for(Map.Entry<String, VirtualView> entry : clients.entrySet()){

            VirtualView client = entry.getValue();

            MsgOnPlayerDrawnCard message = new MsgOnPlayerDrawnCard(gameRepresentation,playerName, card.getId(), null);

            client.sendMessage(message);

        }
    }

    /**
     * notifies the client that the card has been successfully played
     * @param playerWhoPlayed the gard
     * @param cardPlayedId of the card played
     * @throws RemoteException
     */
    public void notifyOfCardPlayed(String playerWhoPlayed, String cardPlayedId) throws RemoteException {
        GameRepresentation gameRepresentation = getGameRepresentation();

        for(Map.Entry<String, VirtualView> entry : clients.entrySet()){

            VirtualView client = entry.getValue();


            MsgOnPlayerPlayedCard message = new MsgOnPlayerPlayedCard(gameRepresentation, cardPlayedId, playerWhoPlayed);
            client.sendMessage(message);
        }
    }

    /**
     * notifies the client that the objective has been successfully chosen
     * @param playerName to notify
     * @throws RemoteException
     */
    public void notifyObjChosen(String playerName) throws RemoteException {

        GameRepresentation gameRepresentation = getGameRepresentation();

        for(Map.Entry<String, VirtualView> entry : clients.entrySet()){

            VirtualView client = entry.getValue();

            MsgOnPlayerChooseObjective message = new MsgOnPlayerChooseObjective(playerName, gameRepresentation);

            client.sendMessage(message);
        }
    }

    /**
     * notifies the every client that there was an error
     * @param name of the player to notify
     * @param e is the error to be notified
     * @param actionDetails of the error
     * @throws RemoteException
     */
    public void notifyError(String name, PlayerActionError e, String actionDetails) throws RemoteException {
        VirtualView clientOfRequest = clients.get(name);

        if(clientOfRequest == null){
            System.err.println(actionDetails + " from non existent player!");
            return;
        }

        MsgReportError message = new MsgReportError(e.getError());
        clients.get(name).sendMessage(message);
    }

    /**
     * notifies a specific client that there was an error
     * @param client to be notified
     * @param e is the error to be notified
     * @throws RemoteException
     */
    public void notifyErrorSpecificClient(VirtualView client, PlayerActionError e) throws RemoteException {
        MsgReportError message = new MsgReportError(e.getMessage());
        client.sendMessage(message);
    }

    /**
     * notifies the client that the game has been successfully created
     * @param gameId of the game created
     * @param name of the player to notify
     * @param numberOfPlayersLeftToJoin in the game before it starts
     * @throws RemoteException
     */
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

    /**
     * notifies the client that the player has been successfully reconnected
     * @param gameId of the game
     * @param playerName to notify
     * @param playersLeft to reconnect
     * @throws RemoteException
     */
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

    /**
     * notifies the client that a player has successfully joined or that he has successfully joined the game
     * @param gameId it the game
     * @param playerName that has joined
     * @param playersLeft to join before the game starts
     * @throws RemoteException
     */
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

    /**
     * notifies the client that the message has been successfully delivered
     * @param sender of the message
     * @param receiver of the message
     * @param isPrivate boolean that specifies whether the message was public or private
     * @throws RemoteException
     */
    public void notifyChatMessage(String sender, String receiver, boolean isPrivate) throws RemoteException {
        GameRepresentation gameRepresentation = getGameRepresentation();

        for(Map.Entry<String, VirtualView> entry : clients.entrySet()){

            VirtualView client = entry.getValue();

            MsgOnChatMessage message = new MsgOnChatMessage(gameRepresentation, sender, receiver, isPrivate);

            client.sendMessage(message);
        }


    }

    /**
     * notifies the client that the color has been successfully chosen
     * @param name of the player to notify
     * @throws RemoteException
     */
    public void notifyChooseColor(String name) throws RemoteException {
        GameRepresentation gameRepresentation = getGameRepresentation();

        for(Map.Entry<String, VirtualView> entry : clients.entrySet()){

            VirtualView client = entry.getValue();

            MsgOnChooseColor message = new MsgOnChooseColor(name, gameRepresentation);

            client.sendMessage(message);
        }
    }

    /**
     * notifies every client that the game has ended, and notifies who has won
     * @param players inside the game
     * @throws RemoteException
     */
    public void notifyGameEndedWinners(ArrayList<String> players) throws RemoteException {
        GameRepresentation gameRepresentation = getGameRepresentation();
        MsgOnGameWinners msg = new MsgOnGameWinners(players, gameRepresentation);

        for(Map.Entry<String, VirtualView> entry : clients.entrySet()){
            VirtualView client = entry.getValue();
            client.sendMessage(msg);
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

    private void deleteBackUpGame(Game game){
        new BackupManager(game, false).start();
    }

    public void sendPing() throws RemoteException {
        for (VirtualView client : clients.values()){
            client.sendMessage(new MsgPingS2c(MessageTypeS2C.PING));
        }
    }

    /**
     * notifies every client that the game was terminated
     * @throws RemoteException
     */
    public void notifyGameTermination() throws RemoteException {

        for(Map.Entry<String, VirtualView> entry : clients.entrySet()){

            VirtualView client = entry.getValue();
          try{
              client.sendMessage(new MsgOnGameTermination(MessageTypeS2C.TERMINATION));
          }catch (RemoteException ignored){

        }

        }
    }


    public Optional<JoinInfo> getJoinInfo(){
        synchronized (gameModel){return gameModel.getJoinInfo();
        }
    }

}
