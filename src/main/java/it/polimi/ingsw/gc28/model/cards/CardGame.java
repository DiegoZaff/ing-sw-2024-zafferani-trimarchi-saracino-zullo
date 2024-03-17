package it.polimi.ingsw.gc28.model.cards;

import it.polimi.ingsw.gc28.model.Cell;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Table;
import it.polimi.ingsw.gc28.model.Vertex;
import it.polimi.ingsw.gc28.model.resources.Resource;
import it.polimi.ingsw.gc28.model.resources.ResourcePrimary;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class CardGame extends Card {
    private Vertex[] verticesFront;






    public CardGame(Vertex[] verticesFront){

        this.verticesFront = verticesFront;
    }

    /**
     * This method provides the resource primary of the card for counting the points
     * of the position objectives. Its implementation will return Null inside InitialCard.
     */
    public abstract Optional<ResourcePrimary> getObjectiveResource();

    // ? maybe we can merge playFront and playBack by passing an attribute isFront, since
    // ? some of their logic is overlapping. let's see if there's a lot of overlap.

    /**
    * this method play the card in the front verse
    * @param table indicates the table in which the card is played*
    * @param playCoordinate indicate the cordinates in which the card should be played
     */
    public abstract void playFront(Table table, Coordinate playCoordinate);

    /**
     * this method play the card in the back verse
     * @param table indicates the table in which the card is played
     * @param playCoordinate indicate the cordinates in which the card should be played
     */
    public abstract void playBack(Table table, Coordinate playCoordinate);


    public Resource getResourceInsideVertex (int value){

        return verticesFront[value].getResource();
    }





}

