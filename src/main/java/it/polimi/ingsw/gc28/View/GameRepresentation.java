package it.polimi.ingsw.gc28.View;

import it.polimi.ingsw.gc28.model.cards.CardGold;
import it.polimi.ingsw.gc28.model.cards.CardObjective;
import it.polimi.ingsw.gc28.model.cards.CardResource;

import java.util.ArrayList;
import java.util.Map;

public class GameRepresentation {

    private String myNickname;

    private ArrayList<String> othersNickname;


    private ArrayList<CardObjective> globalObjectives;
    private ArrayList<CardResource> faceUpResourceCards;
    private ArrayList<CardGold> faceUpGoldCards;


    //next card to be drawn, needed to show the back of the card. Can be substituted with the card primary resource
    private CardResource nextResourceCard;
    private CardGold nextGoldCard;

    private Map<String, Integer> points;

    private PrivateRepresentation myRepresentation;

    private Map<String, PrivateRepresentation> othersRepresentation;





}
