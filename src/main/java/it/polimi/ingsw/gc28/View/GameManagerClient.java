package it.polimi.ingsw.gc28.View;

import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.model.cards.Card;
import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.cards.CardObjective;
import it.polimi.ingsw.gc28.model.cards.CardResource;
import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

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

    public void showHand(){
        PrivateRepresentation repr = getPrivateRepresentation(playerName);

        ArrayList<CardResource> cards =  repr.getHand();

        String cardIdsString = cards.stream().map((CardResource::toString)).collect(Collectors.joining(", "));

        this.writeInConsole(String.format("You hand is composed of cards: %s", cardIdsString));

    }

    public void showCardInitial(){

    }

    public void showTable(String name){

    }

    public void showTable(){

    }


    public void showPoints(){

    }

    public void showPlayerAndActionOfTurn(){
        GameRepresentation rep = getCurrentRepresentation();

        String playerToPlay = rep.getPlayerToPlay();
        String action = rep.getActionExpected().name();

        this.writeInConsole(String.format("%s is playing and is acton is %s", playerToPlay, action));

    }

    public void showDrawableCards(){
        GameRepresentation rep = getCurrentRepresentation();

        ArrayList<String> visibleGolds = rep.getFaceUpGoldCards();
        ArrayList<String> visibleCards = rep.getFaceUpResourceCards();
        String nextResource = rep.getNextResourceCard();
        String nextGold = rep.getNextGoldCard();
        visibleCards.addAll(visibleGolds);
        visibleCards.add(nextResource);
        visibleCards.add(nextGold);

        String cardIdsString = String.join(", ", visibleCards);

        this.writeInConsole(String.format("Drawable cards are: %s", cardIdsString));

    }

    public void showGlobalObjectives(){
        GameRepresentation rep = getCurrentRepresentation();

        ArrayList<String> globalObjectives = rep.getGlobalObjectives();
        this.writeInConsole(String.format("Global objectives are %s and %s", globalObjectives.get(0), globalObjectives.get(1)));
    }

    public void showYourObjective(){
        PrivateRepresentation rep = getPrivateRepresentation(playerName);

        CardObjective secretObjective = rep.getPrivateObjective();

        String cardId = secretObjective.toString();
        this.writeInConsole(String.format("Your secret objective is", cardId));
    }

    public void showGlobalChat(){

    }

    public void showPrivateChat(String player){

    }

    private PrivateRepresentation getPrivateRepresentation(String name){
        PrivateRepresentation repr = currentRepresentation.getRepresentations().get(playerName);

        if(repr == null){
            throw new RuntimeException();
        }

        return repr;
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
