package it.polimi.ingsw.gc28.controller;
import it.polimi.ingsw.gc28.model.errors.types.InvalidReceiverName;
import it.polimi.ingsw.gc28.network.messages.client.MessageC2S;
import it.polimi.ingsw.gc28.view.GameRepresentation;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.cards.CardObjective;
import it.polimi.ingsw.gc28.model.cards.CardResource;
import it.polimi.ingsw.gc28.model.cards.CardsManager;
import it.polimi.ingsw.gc28.model.chat.ChatMessage;
import it.polimi.ingsw.gc28.model.errors.types.NoSuchCardId;
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

    public void addPlayerToGame(String name, VirtualView client, boolean notifyJoin) throws RemoteException {

        synchronized (gameModel){
            try{
                this.gameModel.addPlayerToGame(name);
                this.clients.put(name, client);
            }catch (PlayerActionError e){
                // notify error to player
                MsgReportError message = new MsgReportError(name, e.getError());
                client.sendMessage(message);
                return;
            }


            if(notifyJoin){
                int playersLeftToJoin = gameModel.getNPlayers() - gameModel.getActualNumPlayers();

                MsgOnGameJoined message = new MsgOnGameJoined(this.gameModel.getGameId() ,name, playersLeftToJoin);

                clients.get(name).sendMessage(message);
            }

            if(hasGameStarted()){
                for(Map.Entry<String, VirtualView> entry : clients.entrySet()){

                    VirtualView cli = entry.getValue();

                    GameRepresentation representation = getGameRepresentation();

                    MsgOnGameStarted m = new MsgOnGameStarted(representation);

                    cli.sendMessage(m);
                }
            }
        }
    }

    public boolean hasGameStarted(){
        synchronized (gameModel){
            return gameModel.getHasGameStarted();
        }
    }


    /**
     * Overloading, in case we have already calculated Player.
     */
    public Optional<ArrayList<CardObjective>> getPersonalObjectives(Player player){
        synchronized (gameModel) {
            return player.getObjectivesToChoose();
        }
    }

    public ArrayList<CardResource> getPlayerHand(Player player){
        synchronized (gameModel) {
            return player.gethand();
        }
    }


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

            }catch (PlayerActionError e){
                MsgReportError message = new MsgReportError(playerName, e.getError());
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

        MsgReportError message = new MsgReportError(name, actionDetails);
        clients.get(name).sendMessage(message);
    }

    public void notifyGameCreated(String gameId, String name, int numberOfPlayersLeftToJoin) throws RemoteException {
        VirtualView clientOfRequest = clients.get(name);

        if(clientOfRequest == null){
            System.err.println(" from non existent player!");
            return;
        }

        MsgOnGameCreated message = new MsgOnGameCreated(gameId, name, numberOfPlayersLeftToJoin);

        clients.get(name).sendMessage(message);

    }

    public void notifyChatMessage() throws RemoteException {
        GameRepresentation gameRepresentation = getGameRepresentation();

        for(Map.Entry<String, VirtualView> entry : clients.entrySet()){

            VirtualView client = entry.getValue();

            MsgOnChatMessage message = new MsgOnChatMessage(gameRepresentation);

            client.sendMessage(message);
        }
    }

    public GameRepresentation getGameRepresentation(){
        synchronized (gameModel){
            return gameModel.getGameRepresentation();
        }
    }
}
