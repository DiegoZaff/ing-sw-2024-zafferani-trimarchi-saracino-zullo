package it.polimi.ingsw.gc28.view;

import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.model.chat.Chat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class GameRepresentation implements Serializable {
    /*
    CardsManager.getInstance().getCardFromId(id)
     */
    private String playerToPlay;

    private ActionType actionExpected;

    private ArrayList<String> nicknames;
    private  ArrayList<String> globalObjectives, faceUpResourceCards, faceUpGoldCards;
    //next card to be drawn, needed to show the back of the card. Can be substituted with the card primary resource
    private  String nextResourceCard, nextGoldCard;
    private Map<String, Integer> points;
    private Map<String, PrivateRepresentation> representations;
    private Chat chat;

    private boolean isGameAborted;

    public GameRepresentation (String playerToPlay, ActionType actionExpected, ArrayList<String> nicknames, ArrayList<String> globalObjectives,
                               ArrayList<String> faceUpResourceCards, ArrayList<String> faceUpGoldCards,
                               String nextResourceCard, String nextGoldCard,
                               Map<String, Integer> points, Map<String, PrivateRepresentation> representations, Chat chat){
        this.playerToPlay = playerToPlay;
        this.actionExpected = actionExpected;
        this.nicknames = nicknames;
        this.globalObjectives = globalObjectives;
        this.faceUpResourceCards = faceUpResourceCards;
        this.faceUpGoldCards = faceUpGoldCards;
        this.nextResourceCard = nextResourceCard;
        this.nextGoldCard = nextGoldCard;
        this.points = points;
        this.representations = representations;
        this.chat = chat;
        isGameAborted = false;
    }

    /**
     * Empty Constructor.
     */
    public GameRepresentation(){

    }


    public ArrayList<String> getNicknames() {
        return nicknames;
    }

    public ArrayList<String> getGlobalObjectives() {
        return globalObjectives;
    }

    public ArrayList<String> getFaceUpResourceCards() {
        return faceUpResourceCards;
    }

    public ArrayList<String> getFaceUpGoldCards() {
        return faceUpGoldCards;
    }

    public String getNextGoldCard() {
        return nextGoldCard;
    }

    public String getNextResourceCard() {
        return nextResourceCard;
    }

    public Map<String, Integer> getPoints() {
        return points;
    }

    public Map<String, PrivateRepresentation> getRepresentations() {
        return representations;
    }

    public String getPlayerToPlay() {
        return playerToPlay;
    }

    public ActionType getActionExpected() {
        return actionExpected;
    }

    public Chat getChat(){
        return chat;
    }

    public boolean isGameAborted() {
        return isGameAborted;
    }

    public void setGameAborted(boolean gameAborted) {
        isGameAborted = gameAborted;
    }
}
