package it.polimi.ingsw.gc28.View;

import java.util.ArrayList;
import java.util.Map;

public class GameRepresentation {
    /*
    CardsManager.getInstance().getCardTipoFromid(id)
     */
    private ArrayList<String> nicknames;
    private ArrayList<String> globalObjectives, faceUpResourceCards, faceUpGoldCards;
    //next card to be drawn, needed to show the back of the card. Can be substituted with the card primary resource
    private String nextResourceCard, nextGoldCard;
    private Map<String, Integer> points;
    private Map<String, PrivateRepresentation> representations;

    public GameRepresentation (ArrayList<String> nicknames, ArrayList<String> globalObjectives,
                               ArrayList<String> faceUpResourceCards, ArrayList<String> faceUpGoldCards,
                               String nextResourceCard, String nextGoldCard,
                               Map<String, Integer> points, Map<String, PrivateRepresentation> representations){

        this.nicknames = nicknames;
        this.globalObjectives = globalObjectives;
        this.faceUpResourceCards = faceUpResourceCards;
        this.faceUpGoldCards = faceUpGoldCards;
        this.nextResourceCard = nextResourceCard;
        this.nextGoldCard = nextGoldCard;
        this.points = points;
        this.representations = representations;
    }
    //this method update the representation, it is in the message coming from the server
    //TODO: need to be implemented
    public void update(GameRepresentation gameRepresentation){

    }
    
    


}
