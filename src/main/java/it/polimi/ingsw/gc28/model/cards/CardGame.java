package it.polimi.ingsw.gc28.model.cards;

import it.polimi.ingsw.gc28.model.Vertex;
import it.polimi.ingsw.gc28.model.resources.Resource;
import it.polimi.ingsw.gc28.model.resources.ResourcePrimary;

import java.util.HashMap;
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


    public Resource getResourceInsideVertex (int value){

        return verticesFront[value].getResource();
    }



}

