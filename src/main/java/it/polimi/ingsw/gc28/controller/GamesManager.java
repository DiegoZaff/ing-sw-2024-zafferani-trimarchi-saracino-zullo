package it.polimi.ingsw.gc28.controller;

import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.network.messages.client.MessageC2S;
import it.polimi.ingsw.gc28.network.messages.client.MessageTypeC2S;
import it.polimi.ingsw.gc28.network.messages.client.MsgCreateGame;
import it.polimi.ingsw.gc28.network.messages.client.MsgJoinGame;
import it.polimi.ingsw.gc28.network.rmi.GameStub;
import it.polimi.ingsw.gc28.network.rmi.VirtualServer;
import it.polimi.ingsw.gc28.network.rmi.VirtualStub;
import it.polimi.ingsw.gc28.network.rmi.utils.StubRegister;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
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
            createGame(msg.getClient(), msg.getUserName(), msg.getNumberOfPlayers());
        }else if(message.getType().equals(MessageTypeC2S.JOIN_GAME)){
            MsgJoinGame msg = (MsgJoinGame) message;

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
                msg.execute(controller.get());

                VirtualStub stub = new GameStub(controller.get(), msg.getUserName(), msg.getGameId().get());

                msg.getClient().attachGameStub(stub);

            }catch (RemoteException e){
                System.err.println(e.getMessage());
            }


            } else{
            System.err.printf("Message of type %s directed to gamesManager!%n", message.getType());


        }
    }

    public void createGame(VirtualView client, String playerName, int numberOfPlayers)  {
        String gameId = UUID.randomUUID().toString();
        GameController newController = null;
        try {
            newController = new GameController(new Game(numberOfPlayers, gameId));
        } catch (IOException e) {
            System.err.println("Error in creating a GameController: " + e.getMessage());
            throw new RuntimeException(e);
        }

        try{
            newController.addPlayerToGame(playerName, client, false);
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


    public void restoreGame(Game game){

        if(game == null){
            throw new RuntimeException("Game is null, wake up!");
        }

        String gameId = game.getGameId();

        GameController controller = new GameController(game);


        mapGames.put(gameId, controller);

        controller.waitForReconnections();

    }


    public Optional<GameController> getGameController(String id){
        GameController controller = mapGames.get(id);

        return Optional.ofNullable(controller);
    }



}
