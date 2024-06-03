package it.polimi.ingsw.gc28.controller;

import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.model.errors.types.FailedActionManaged;
import it.polimi.ingsw.gc28.model.utils.JoinInfo;
import it.polimi.ingsw.gc28.network.messages.client.*;
import it.polimi.ingsw.gc28.network.messages.server.MsgOnGameStarted;
import it.polimi.ingsw.gc28.network.messages.server.MsgOnJoinableGames;
import it.polimi.ingsw.gc28.network.rmi.GameStub;
import it.polimi.ingsw.gc28.network.rmi.VirtualServer;
import it.polimi.ingsw.gc28.network.rmi.VirtualStub;
import it.polimi.ingsw.gc28.network.rmi.utils.StubRegister;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;
import it.polimi.ingsw.gc28.view.GameRepresentation;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GamesManager {

    private final BlockingQueue<MessageC2S> messageQueue;

    private static GamesManager instance;

    private final Map<String, GameController> mapGames;

    private GamesManager() {
        mapGames = new HashMap<>();
        messageQueue = new LinkedBlockingQueue<>();
        this.processIncomingMessages();
    }

    public static GamesManager getInstance() {
        if (instance == null) {
            instance = new GamesManager();
        }
        return instance;
    }

    /**
     * This method creates and starts a thread which is responsible for popping messages from the queue
     * and processing theme.
     */
    private void processIncomingMessages() {
        new Thread(() -> {
            while (true) {
                try {
                    MessageC2S message = messageQueue.take(); // Blocking call
                    try{
                        this.executeClientMessage(message);
                    }catch (RemoteException e){
                        System.err.println("Remote Exception while creating a game!");
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

    /**
     * This method adds a message to the queue
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
     * The method receives a message from the client and call a controller's method to execute the client's request.
     * @param message is the message coming from the client.
     */
    private void executeClientMessage(MessageC2S message) throws RemoteException {
        if(message.getType().equals(MessageTypeC2S.CREATE_GAME)){
            MsgCreateGame msg = (MsgCreateGame) message;
            createGame(msg);
        }else if(message.getType().equals(MessageTypeC2S.JOIN_GAME)){
            MsgJoinGame msg = (MsgJoinGame) message;
            joinGame(msg);
        }else if(message.getType().equals(MessageTypeC2S.RECONNECT)){
            MsgReconnect msg = (MsgReconnect) message;
            reconnectToGame(msg);}
        else if(message.getType().equals(MessageTypeC2S.JOINABLE_GAMES)){
            MsgJoinableGames msg = (MsgJoinableGames) message;
            sendJoinableGames(msg);
        }else {
        System.err.printf("Message of type %s directed to gamesManager!%n", message.getType());
    }
    }

    public void createGame(MsgCreateGame msg)  {
        int numberOfPlayers = msg.getNumberOfPlayers();
        String playerName = msg.getUserName();
        VirtualView client = msg.getClient();

        String gameId = UUID.randomUUID().toString();
        GameController newController = null;
        try {
            newController = new GameController(new Game(numberOfPlayers, gameId));
        } catch (IOException e) {
            System.err.println("Error in creating a GameController: " + e.getMessage());
            throw new RuntimeException(e);
        }

        try{
            newController.addPlayerToGame(playerName, client);
            VirtualStub stub = new GameStub(newController, playerName, gameId);
            client.attachGameStub(stub);
        } catch (Exception e){
            System.err.println(e.getMessage());
            return;
        }

        mapGames.put(gameId, newController);

        try {
            newController.notifyGameCreated(gameId, playerName, numberOfPlayers - 1);
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void joinGame(MsgJoinGame msg)  {
        if(msg.getGameId().isEmpty()){
            System.err.println("No game id in joinGame message!");
            return;
        }

        Optional<GameController> controller = getGameController(msg.getGameId().get());

        if(controller.isEmpty()) {
            System.err.println("Error");
            return;
        }
        try{
            // TODO : mayber should separate these block of instructions in different try catchs
            msg.execute(controller.get());

            VirtualStub stub = new GameStub(controller.get(), msg.getUserName(), msg.getGameId().get());
            msg.getClient().attachGameStub(stub);

            int playersLeft = controller.get().getPlayersToJoin();
            controller.get().notifyPlayerJoined(msg.getGameId().get(), msg.getUserName(), playersLeft);

            controller.get().hasGameStarted();
        }catch (RemoteException e){
            System.err.println(e.getMessage());
        } catch (FailedActionManaged e) {
            System.err.println("Error already managed by controller: " + e.getMessage());
        }
    }

    public void reconnectToGame(MsgReconnect msg){
        if(msg.getGameId().isEmpty()){
            System.err.println("No game id in reconnect message!");
            return;
        }

        Optional<GameController> controller = getGameController(msg.getGameId().get());

        if(controller.isEmpty()) {
            System.err.println("Error, no controller associated to gameId " + msg.getGameId());
            return;
        }

        try{
            msg.execute(controller.get());

            VirtualStub stub = new GameStub(controller.get(), msg.getPlayerName(), msg.getGameId().get());
            msg.getClient().attachGameStub(stub);

            int playersLeft = controller.get().getPlayersToReconnect();
            controller.get().notifyPlayerReconnected(msg.getGameId().get(), msg.getPlayerName(), playersLeft);

            controller.get().hasGameRestarted();
        }catch (RemoteException e){
            System.err.println(e.getMessage());
        } catch (FailedActionManaged e) {
            System.err.println("Error already managed by controller: " + e.getMessage());
        }
    }


    public void restoreGame(Game game){

        if(game == null){
            throw new RuntimeException("Game is null, wake up!");
        }

        String gameId = game.getGameId();

        GameController controller = new GameController(game);


        mapGames.put(gameId, controller);

        controller.waitForReconnections();

    }

    private void sendJoinableGames(MsgJoinableGames msg){
        VirtualView client = msg.getClient();

        if(client == null){
            return;
        }

        ArrayList<JoinInfo> res = new ArrayList<>();
        mapGames.forEach((id, controller) -> {
            Optional<JoinInfo> info = controller.getJoinInfo();
            info.ifPresent(res::add);
        });


        MsgOnJoinableGames msgToSend = new MsgOnJoinableGames(res);
        try {
            client.sendMessage(msgToSend);
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
        }
    }


    public Optional<GameController> getGameController(String id){
        GameController controller = mapGames.get(id);

        return Optional.ofNullable(controller);
    }
}
