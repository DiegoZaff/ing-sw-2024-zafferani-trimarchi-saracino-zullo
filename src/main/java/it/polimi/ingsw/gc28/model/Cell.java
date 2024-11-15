package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.resources.Resource;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

/**
 * this class represents a cell, and is used to store information about the card played,
 * the moment the card was played and the side of the card
 */
public class Cell implements Serializable {
    /**
     * is the card played in the cell
     */
    private final CardGame card;

    /**
     * is used to reconstruct the order of play for each table
     */
    private final int orderPlay;

    /**
     * gives information of the side the card has been played on
     */
    private final boolean isPlayedFront;

    public boolean getIsPlayedFront(){
        return isPlayedFront;
    }


    public Cell(CardGame card, int orderPlay, boolean isPlayedFront) {
        this.card = card;
        this.orderPlay = orderPlay;
        this.isPlayedFront = isPlayedFront;
    }


    /**
     * this method provides the points gained when a card is played
     * @return the points if the card is played front, 0 if the card is played back
     */
    public int points(Table table, Coordinate coordinate){
        if (isPlayedFront){
            return card.getPoints(table,coordinate);
        }else{
            return 0;
        }
    }

    public Map<Resource, Integer> getResources (){
        if (isPlayedFront){
            return card.getFrontCardResource();
        } else {
            return card.getBackCardResource();
        }
    }


    private Vertex [] getVertex (){
        Vertex [] v;
        if (isPlayedFront){
            v = card.getVerticesFront();
        } else {
            v = card.getVerticesBack();
        }
        return v;
    }

    public CardGame getCard() {
        return card;
    }

    public Optional<Resource> getNWResource (){
        return getVertex()[0].getResource();
    }

    public boolean getNWExists(){
        return getVertex()[0].isExists();
    }

    public Optional<Resource> getNEResource (){
        return getVertex()[1].getResource();
    }

    public boolean getNEExists(){
        return getVertex()[1].isExists();
    }

    public Optional<Resource> getSEResource (){
        return getVertex()[2].getResource();
    }

    public boolean getSEExists(){
        return getVertex()[2].isExists();
    }
    public Optional<Resource> getSWResource (){
        return getVertex()[3].getResource();
    }

    public boolean getSWExists(){
        return getVertex()[3].isExists();
    }

    public int getOrderPlay() {
        return orderPlay;
    }
}
