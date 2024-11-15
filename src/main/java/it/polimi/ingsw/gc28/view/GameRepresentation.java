package it.polimi.ingsw.gc28.view;

import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.model.chat.Chat;
import it.polimi.ingsw.gc28.model.utils.PlayerColor;
import it.polimi.ingsw.gc28.view.utils.PlayerStatusInfo;

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
    private String nextResourceCard, nextGoldCard;
    private Map<String, Integer> points;
    private Map<String, PrivateRepresentation> representations;
    private Chat chat;

    // max number of players.
    private int nPlayers;

    private boolean isGameAborted;

    private Integer roundsLeft;

    public GameRepresentation (String playerToPlay, ActionType actionExpected, ArrayList<String> nicknames, ArrayList<String> globalObjectives,
                               ArrayList<String> faceUpResourceCards, ArrayList<String> faceUpGoldCards,
                               String nextResourceCard, String nextGoldCard,
                               Map<String, Integer> points, Map<String, PrivateRepresentation> representations, Chat chat, int nPlayers, Integer roundsLeft){
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
        this.nPlayers = nPlayers;
        this.roundsLeft = roundsLeft;
        this.isGameAborted = false;
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

    public int getNPlayers() {
        return nPlayers;
    }

    public PrivateRepresentation getPrivateRepresentationOf(String name){
        return getRepresentations().get(name);
    }

    public Integer getRoundsLeft() {
        return roundsLeft;
    }

    public PlayerStatusInfo getPlayerStatusInfo(String player){
        PrivateRepresentation privateRep = getPrivateRepresentationOf(player);

        if(privateRep == null){
            return null;
        }

        if(points.get(player) == null){
            System.err.println("Private repr has bad state! player has null points");
            return null;
        }

        return  new PlayerStatusInfo(player, privateRep.getColor(), points.get(player), privateRep.isWinner(), getRoundsLeft(), actionExpected, playerToPlay);

    }

    public PlayerColor getPlayerColor(String name){
        return this.getPrivateRepresentationOf(name).getColor();
    }
}
