package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.cards.Card;
import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.objectives.Objective;
import it.polimi.ingsw.gc28.model.resources.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Cell {

    public final CardGame card;

    private final int orderPlay;

    private final boolean isPlayedFront;


    public Cell(CardGame card, int orderPlay, boolean isPlayedFront) {
        this.card = card;
        this.orderPlay = orderPlay;
        this.isPlayedFront = isPlayedFront;
    }


    /**
     * this method provides the points gained when a card is played
     * @return the points if the card is played front, 0 if the card is played back
     */
    public int Points(Table table, Coordinate coordinate){
        if (isPlayedFront){
            return card.getPoints(table,coordinate);
        }else{
            return 0;
        }
    }

    public Map<Resource, Integer> getResources (){
        if (isPlayedFront){
            return card.getFrontCardVertexResource();
        } else {
            return card.getMapCentralResource();
        }
    }

    public Optional<Resource> getNWResource (){
        Vertex []v;
        if (isPlayedFront){
            v = card.getVerticesFront();
        } else {
            v = card.getVerticesBack();
        }
        return v[0].getResource();
    }
    public Optional<Resource> getNEResource (){
        Vertex []v;
        if (isPlayedFront){
            v = card.getVerticesFront();
        } else {
            v = card.getVerticesBack();
        }
        return v[1].getResource();
    }
    public Optional<Resource> getSEResource (){
        Vertex []v;
        if (isPlayedFront){
            v = card.getVerticesFront();
        } else {
            v = card.getVerticesBack();
        }
        return v[2].getResource();
    }
    public Optional<Resource> getSWResource (){
        Vertex []v;
        if (isPlayedFront){
            v = card.getVerticesFront();
        } else {
            v = card.getVerticesBack();
        }
        return v[3].getResource();
    }
    //potremmo modificare questi quattro metodi, da rivedere
}
