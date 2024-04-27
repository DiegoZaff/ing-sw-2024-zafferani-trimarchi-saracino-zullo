package it.polimi.ingsw.gc28.controller;

import it.polimi.ingsw.gc28.model.Coordinate;
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
    public void processIncomingMessages() {
        new Thread(() -> {
            while (true) {
                try {
                    MessageC2S message = messageQueue.take(); // Blocking call
                    try{
                        this.executeClientMessage(message);
                    }catch (IOException e){
                        System.err.println("Message threw an error!");
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
    public void executeClientMessage(MessageC2S message) throws IOException {
        Optional<String> gameId = message.getGameId();

        if(gameId.isEmpty()){
            MsgCreateGame messageCreateGame = (MsgCreateGame) message;

            int nPlayers = messageCreateGame.getNumberOfPlayers();
            VirtualView client = messageCreateGame.getVirtualView();
            String playerName = messageCreateGame.getUserName();

            createGame(client, playerName, nPlayers);
        }
        else {
            Optional<GameController> controller = getGameController(String.valueOf(gameId));

            if(controller.isEmpty()){
                System.err.println("Error");
                return;
            }

            message.execute(controller.get());
        }
    }

    public void createGame(VirtualView client, String playerName, int numberOfPlayers)  {
        GameController newController = null;
        try {
            newController = new GameController(new Game(numberOfPlayers));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try{
            newController.addPlayerToGame(playerName, client);
        }catch (RuntimeException e){
            System.err.println("Error in game creation.");
            return;
        }

        String gameId = UUID.randomUUID().toString();
        mapGames.put(gameId, newController);

        newController.notifyGameCreated(gameId, playerName, numberOfPlayers - 1);
    }


    public void joinGame(VirtualView client, String gameId, String userName){
        Optional<GameController> controller = getGameController(gameId);

        if(controller.isEmpty()){
            try {
                client.reportError("This game doesn't exist!");
                return;
            } catch (RemoteException e) {
                System.err.println(e.getMessage());
                return;
            }
        }

        try {
            controller.get().addPlayerToGame(userName, client);
        }catch (RuntimeException e){
            System.err.println(e.getMessage());
        }

    }

    public void playGameCard(String playerName, String gameId, String cardId, boolean isFront, Coordinate coordinate){
        Optional<GameController> controller = getGameController(gameId);

        if(controller.isEmpty()){
            System.err.println("PlayCard requested from non-existent game");
            return;
        }

        controller.get().playCard(playerName, cardId, isFront, coordinate);

    }

    public void drawCard(String playerName, String gameId, boolean fromGoldDeck){
        Optional<GameController> controller = getGameController(gameId);

        if(controller.isEmpty()){
            System.err.println("DrawCard from decks requested from non-existent game");
            return;
        }

        controller.get().drawCard(playerName, fromGoldDeck);
    }

    public void drawCard(String playerName, String gameId, String cardId){
        Optional<GameController> controller = getGameController(gameId);

        if(controller.isEmpty()){
            System.err.println("DrawCard form visible cards requested from non-existent game");
            return;
        }

        controller.get().drawCard(playerName, cardId);
    }

    public void chooseObjective(String playerName, String gameId, String cardId){
        Optional<GameController> controller = getGameController(gameId);

        if(controller.isEmpty()){
            System.err.println("ChooseObjective requested from non-existent game");
            return;
        }

        controller.get().chooseObjectivePersonal(playerName, cardId);
    }

    public Optional<GameController> getGameController(String id){
        GameController controller = mapGames.get(id);

        return Optional.ofNullable(controller);
    }



}
