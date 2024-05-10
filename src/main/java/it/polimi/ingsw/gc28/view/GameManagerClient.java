package it.polimi.ingsw.gc28.view;

import it.polimi.ingsw.gc28.model.cards.*;
import it.polimi.ingsw.gc28.view.utils.TuiStringHelper;
import it.polimi.ingsw.gc28.model.Table;

import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;

import java.util.ArrayList;
import java.util.Map;
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




    public void showHand(boolean isFront){
        PrivateRepresentation repr = getPrivateRepresentation(playerName);

        ArrayList<CardResource> cards =  repr.getHand();
        String show = (" \n \n \n \n ");
        for (CardResource cardResource : cards){
            show = this.mergeCards(show, cardResource.toString(isFront));
        }
        /*
        if(cards.size() >= 2){
            CardGame card1 = cards.getFirst();
            CardGame card2 = cards.get(1);

            ArrayList<String> verticesStrings1 = TuiStringHelper.getVerticesStringInfo(card1, isFront);
            ArrayList<String> verticesStrings2 = TuiStringHelper.getVerticesStringInfo(card2, isFront);


            String centralRes1 = card1.getCentralResourceStringInfo(isFront);
            String centralRes2 = card2.getCentralResourceStringInfo(isFront);

            String show = null;

            if(cards.size() == 2){
                show = String.format("""
                    _________________   _________________
                    |%s           %s|   |%s           %s|
                    |       %s      |   |       %s      |
                    |%s           %s|   |%s           %s|
                    ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾   ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
                        %s              %s
                """, verticesStrings1.get(0), verticesStrings1.get(1), verticesStrings2.get(0), verticesStrings2.get(1),centralRes1, centralRes2,
                        verticesStrings1.get(3), verticesStrings1.get(2), verticesStrings2.get(3), verticesStrings2.get(2), card1.getId(), card2.getId());
            }else if(cards.size() == 3){

                CardGame card3 = cards.get(2);

                ArrayList<String> verticesStrings3 = TuiStringHelper.getVerticesStringInfo(card3, isFront);

                String centralRes3 = card3.getCentralResourceStringInfo(isFront);

                show = String.format("""
                    _________________   _________________   _________________
                    |%s           %s|   |%s           %s|   |%s           %s|
                    |       %s      |   |       %s      |   |       %s      |
                    |%s           %s|   |%s           %s|   |%s           %s|
                    ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾   ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾   ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
                          %s              %s              %s
                """, verticesStrings1.get(0), verticesStrings1.get(1), verticesStrings2.get(0), verticesStrings2.get(1),
                        verticesStrings3.get(0), verticesStrings3.get(1), centralRes1, centralRes2, centralRes3,
                        verticesStrings1.get(3), verticesStrings1.get(2), verticesStrings2.get(3), verticesStrings2.get(2),
                        verticesStrings3.get(3), verticesStrings3.get(2),card1.getId(), card2.getId(), card3.getId());

            }
            */
            this.writeInConsole(String.format("Your hand is composed of cards: \n%s", show));



    }

    private String mergeCards(String s1, String s2){
        String []a1 = s1.split("\n");
        String []a2 = s2.split("\n");
        StringBuffer show = new StringBuffer();
        for (int i = 0; i<a1.length; i++){
            show.append(a1[i]).append("   ").append(a2[i]).append("\n");
        }
        return show.toString();
    }

    public void showCardInitial(){
        PrivateRepresentation repr = getPrivateRepresentation(playerName);

        CardInitial cardInitial = repr.getCardInitial();

        if(cardInitial == null){
            this.writeInConsole("CardInitial is null!");
        }else{
            String show = this.mergeCards(cardInitial.toString(true), cardInitial.toString(false));
            this.writeInConsole(String.format("You card initial is (left:front right:back): \n%s", show));
        }
    }

    public void showTable(String name){
        Table table = getPrivateRepresentation(name).getTable();

        this.writeInConsole(table.toString());
    }

    public void showTable(){
        showTable(playerName);
    }


    public void showPoints(){
        Map<String, Integer> points = currentRepresentation.getPoints();

        StringBuilder pointsString = new StringBuilder();


        for(Map.Entry<String, Integer> entry : points.entrySet() ){
            pointsString.append(String.format("%s : %s \n", entry.getKey(), entry.getValue()));
        }

        this.writeInConsole(pointsString.toString());
    }

    public void showPlayerAndAction(){
        GameRepresentation rep = getCurrentRepresentation();

        String playerToPlay = rep.getPlayerToPlay();
        String action = rep.getActionExpected().name();

        this.writeInConsole(String.format("%s is playing and is acton is %s", playerToPlay, action));

    }

    public void showDrawableCards(){
        GameRepresentation rep = getCurrentRepresentation();

        ArrayList<String> visibleGolds = rep.getFaceUpGoldCards();
        ArrayList<String> visibleResource = rep.getFaceUpResourceCards();
        String nextResource = rep.getNextResourceCard();
        String nextGold = rep.getNextGoldCard();

        this.writeInConsole(String.format("Drawable face up gold cards are:\n%s", this.drawableCard(visibleGolds)));
        this.writeInConsole(String.format("Drawable face up resource cards are:\n%s", this.drawableCard(visibleResource)));
        this.writeInConsole(String.format("next gold cards is:\n%s", this.drawableCard(nextGold)));
        this.writeInConsole(String.format("next resource cards is:\n%s", this.drawableCard(nextResource)));


    }
    private String drawableCard(ArrayList <String> cards){
        String show = (" \n \n \n \n ");
        for(String s : cards){
            CardResource c = CardsManager.getInstance().getCardResourceFromId(s).get();
            show = mergeCards(show, c.toString(true));
        }
        return show;
    }

    private String drawableCard(String card){
        return CardsManager.getInstance().getCardResourceFromId(card).get().toString(false);
    }

    public void showGlobalObjectives(){
        GameRepresentation rep = getCurrentRepresentation();

        ArrayList<String> globalObjectives = rep.getGlobalObjectives();
        this.writeInConsole(String.format("Global objectives are %s and %s", globalObjectives.get(0), globalObjectives.get(1)));
    }

    public void showYourObjective(){
        PrivateRepresentation rep = getPrivateRepresentation(playerName);

        CardObjective secretObjective = rep.getPrivateObjective();

        String cardId;
        if(secretObjective != null){
            cardId = secretObjective.toString();
        }else{
            cardId = "null";
        }
        this.writeInConsole(String.format("Your secret objective is %s", cardId));
    }

    public void showObjectivesToChoose(){
        PrivateRepresentation rep = getPrivateRepresentation(playerName);

        ArrayList<CardObjective> cards = rep.getObjsToChoose();

        String result;

        if(cards == null || cards.size() != 2){
            result = "no cards to choose yet";
        }else{
            result = String.format("%s, %s", cards.get(0).getId(), cards.get(1).getId());
        }

        this.writeInConsole(result);

    }

    public void showGlobalChat(){
        GameRepresentation rep = getCurrentRepresentation();
        String chat = rep.getChat().toString();
        this.writeInConsole(String.format("%s", chat));
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
