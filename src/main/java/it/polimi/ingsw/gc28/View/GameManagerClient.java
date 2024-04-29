package it.polimi.ingsw.gc28.View;

import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GameManagerClient {

    private static GameManagerClient instance;

    private GameRepresentation currentRepresentation;

    private final BlockingQueue<MessageS2C> messageQueue;

    private String gameId;

    private String playerName;


    private GameManagerClient() {
        this.gameId = null;
        this.playerName = null;
        this.messageQueue = new LinkedBlockingQueue<>();
        processIncomingMessages();
    }

    private void processIncomingMessages() {
        new Thread(() -> {
            while (true) {
                try {
                    MessageS2C message = messageQueue.take(); // Blocking call

                    message.update(this);

                } catch (InterruptedException e) {
                    System.err.println("Thread was interrupted while taking a message!");
                    System.err.println(e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public static GameManagerClient getInstance() {
        if (instance == null) {
            instance = new GameManagerClient();
        }
        return instance;
    }


    /**
     * This method adds a message to the queue
     */
    public void addMessageToQueue(MessageS2C message){
        try {
            messageQueue.put(message);
        } catch (InterruptedException e) {
            System.err.println("Thread was interrupted while inserting a message!");
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * This is responsible for writing in console. It's synchronized so that there's no overlapping between 2 messages
     */
    public synchronized void writeInConsole(String text){
        System.out.println(text);
    }

    public GameRepresentation getCurrentRepresentation() {
        return currentRepresentation;
    }

    public void setCurrentRepresentation(GameRepresentation currentRepresentation) {
        this.currentRepresentation = currentRepresentation;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
