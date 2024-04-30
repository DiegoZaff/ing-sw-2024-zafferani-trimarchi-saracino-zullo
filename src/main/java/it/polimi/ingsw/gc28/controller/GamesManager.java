package it.polimi.ingsw.gc28.controller;

import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.network.messages.client.MessageC2S;
import it.polimi.ingsw.gc28.network.messages.client.MsgCreateGame;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;

import java.io.IOException;
import java.rmi.RemoteException;
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
        Optional<String> gameId = message.getGameId();

        if(gameId.isEmpty()){
            MsgCreateGame messageCreateGame = (MsgCreateGame) message;

            int nPlayers = messageCreateGame.getNumberOfPlayers();
            String playerName = messageCreateGame.getUserName();
            VirtualView client = messageCreateGame.getClient();

            createGame(client, playerName, nPlayers);
        }
        else {
            Optional<GameController> controller = getGameController(gameId.get());

            if(controller.isEmpty()){
                System.err.println("Error");
                return;
            }

            message.execute(controller.get());
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
        }catch (RemoteException e){
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


    public Optional<GameController> getGameController(String id){
        GameController controller = mapGames.get(id);

        return Optional.ofNullable(controller);
    }



}
