package it.polimi.ingsw.gc28.view;

import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.model.cards.*;
import it.polimi.ingsw.gc28.model.utils.PlayerColor;
import it.polimi.ingsw.gc28.model.Table;

import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;
import it.polimi.ingsw.gc28.view.utils.PlayerStatusInfo;
import it.polimi.ingsw.gc28.view.utils.SnackBarMessage;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GameManagerClient {

    private static GameManagerClient instance;

    private GameRepresentation currentRepresentation;

    private final BlockingQueue<MessageS2C> messageQueue;

    private String gameId;

    private String playerName;

    public static boolean isCli = true;

    private ArrayList<GuiObserver> listeners;

    private InfoObserver snackBarListener;

    private Integer nPlayers;

    private Integer playersIn;


    private GameManagerClient() {
        this.gameId = null;
        this.playerName = null;
        this.messageQueue = new LinkedBlockingQueue<>();
        this.listeners = new ArrayList<GuiObserver>();
        processIncomingMessages();
    }

    private void processIncomingMessages() {
        new Thread(() -> {
            while (true) {
                try {
                    MessageS2C message = messageQueue.take(); // Blocking call

                    //Platform.runLater(() -> {
                        message.update(this, isCli);
                   // });

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
     * Check if game is still active
     */
    public boolean canICreateOrJoinAGame(){
        if(currentRepresentation == null){
            return true;
        }

        return currentRepresentation.getActionExpected().equals(ActionType.GAME_ENDED);
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
            for (int i = 0; i< repr.getHand().size(); i++){
                this.writeInConsole("card "+ (i+1) +": "+ repr.getHand().get(i).toString() );
            }



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
            this.writeInConsole("cardId: "+cardInitial.toString());
        }
    }

    public void showTable(String name){
        Table table = getPrivateRepresentation(name).getTable();
        if (playerName.equals(name)){
            this.writeInConsole("your table:");
        }else {
            this.writeInConsole(name+" table:");
        }
        try {
            this.writeInConsole(table.toString());
        }catch (NoSuchElementException e){
            this.writeInConsole(e.getMessage());
        }
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
        String text;

        if (playerToPlay.equals(playerName)){
            text = String.format("It's your turn! your next action is %s", action);
        } else{

            text = String.format("%s is playing and his action is %s", playerToPlay, action);
        }

        this.writeInConsole(text);

    }

    public void showDrawableCards(){
        GameRepresentation rep = getCurrentRepresentation();

        int i;

        ArrayList<String> visibleGolds = rep.getFaceUpGoldCards();
        ArrayList<String> visibleResource = rep.getFaceUpResourceCards();
        String nextResource = rep.getNextResourceCard();
        String nextGold = rep.getNextGoldCard();

        this.writeInConsole(String.format("Drawable face up gold cards are:\n%s", this.drawableCard(visibleGolds)));
        this.cardsArrayId(visibleGolds);
        this.writeInConsole(String.format("Drawable face up resource cards are:\n%s", this.drawableCard(visibleResource)));
        this.cardsArrayId(visibleResource);
        this.writeInConsole(String.format("next gold cards is:\n%s", this.drawableCard(nextGold)));
        this.writeInConsole(String.format("next resource cards is:\n%s", this.drawableCard(nextResource)));


    }

    private void cardsArrayId(ArrayList <String> card){
        for (int i = 0; i < card.size(); i++){
            this.cardId(card.get(i),i);
        }
    }
    private void cardId (String card, int i){
        if (CardsManager.getInstance().getCardGameFromId(card).isPresent()){
            this.writeInConsole("card "+ (i+1)+": "+CardsManager.getInstance().getCardGameFromId(card).get());
        }
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
        String result = mergeCards(CardsManager.getInstance().getCardObjectiveFromId(globalObjectives.get(0)).get().toString(),
                CardsManager.getInstance().getCardObjectiveFromId(globalObjectives.get(1)).get().toString());
        this.writeInConsole(String.format("Global objectives are\n" +
                "%s", result));
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

    public void showColor(){
        PrivateRepresentation rep = getPrivateRepresentation(playerName);

        String col;
        PlayerColor color = rep.getColor();
        if(color == null){
            col = "null";
        } else {
            col = String.valueOf(color);
        }
        this.writeInConsole("Your color is " + col);
    }
    public void showObjectivesToChoose(){
        PrivateRepresentation rep = getPrivateRepresentation(playerName);

        ArrayList<CardObjective> cards = rep.getObjsToChoose();





        if(cards == null || cards.size() != 2){
            this.writeInConsole("no cards to choose yet");
        }else{
            ArrayList<String>cardId = new ArrayList<>();
            cardId.add(cards.get(0).getId());
            cardId.add((cards.get(1).getId()));
            String result = mergeCards(CardsManager.getInstance().getCardObjectiveFromId(cardId.get(0)).get().toString(),
                    CardsManager.getInstance().getCardObjectiveFromId(cardId.get(1)).get().toString());
            this.writeInConsole(result);
            for (int i = 0; i< cards.size(); i++){
                this.writeInConsole("card "+(i+1)+" id: "+cards.get(i).getId());
            }

        }




    }

    public void showGlobalChat(){
        GameRepresentation rep = getCurrentRepresentation();
        String chat = rep.getChat().toString();
        this.writeInConsole(String.format("%s", chat));
    }

    public void showPrivateChat(String player) {
        GameRepresentation rep = getCurrentRepresentation();
        String chat = null;
        ArrayList<String> nicknames = rep.getNicknames();
        for (String name : nicknames) {
            if (name.equals(player)) {
                chat = rep.getChat().toString(playerName, player);
            }
        }
        if (chat != null) {
            this.writeInConsole(String.format("%s", chat));
        } else {
            this.writeInConsole(String.format("Private chat with %s is empty", player));
        }
    }


    private PrivateRepresentation getPrivateRepresentation(String name){
        PrivateRepresentation repr = currentRepresentation.getRepresentations().get(playerName);

        if(repr == null){
            // TODO : why throw exception?
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

    public void terminateGame(){
        if (this.currentRepresentation != null){
            this.writeInConsole("someone disconnected!! the game ended");
            this.currentRepresentation = null;
        }
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

    public void addListeners(GuiObserver obv){
        this.listeners.add(obv);
    }

    public void addListenerAndRemoveOthers(GuiObserver obv){
        cleanAllListeners();
        addListeners(obv);
    }
    public void updateListeners(){
        for (GuiObserver obs : listeners){
            Platform.runLater(() -> {
                obs.update(currentRepresentation);
            });
        }
    }

    // TODO : a cosa serve? non basta un metodo?
    public void updateListeners(MessageS2C message){
        for (GuiObserver obs : listeners){
            Platform.runLater(() -> {
                obs.update(message);
            });
        }
    }

    // todo : should we do some synchronization?
    public void cleanAllListeners(){
        listeners.clear();
    }

    public void addSnackBarListener(InfoObserver listener){
        this.snackBarListener = listener;
    }

    public void updateSnackBarListener(SnackBarMessage msg){

        if(this.snackBarListener != null){
            Platform.runLater(() -> {
                this.snackBarListener.showInSnackBar(msg);
            });
        }
    }

    public int getNPlayers() {
        return nPlayers;
    }

    public void setNPlayers(int nPlayers) {
        this.nPlayers = nPlayers;
    }

    public int getPlayersIn(){
        return playersIn;
    }

    public void setPlayersIn(int playersIn) {
        this.playersIn = playersIn;
    }

    public PrivateRepresentation getMyPrivateRepresentation(){
        return getPrivateRepresentation(playerName);
    }

    public PlayerStatusInfo getMyPlayerStatusInfo(){
        return getCurrentRepresentation().getPlayerStatusInfo(playerName);
    }
}
