package it.polimi.ingsw.gc28.model.cards;

import it.polimi.ingsw.gc28.model.resources.Resource;
import it.polimi.ingsw.gc28.model.Vertex;

public class CardInitial extends CardGame {
    private Vertex[] verticesBack;
    private Resource[] centralResources;
    public CardInitial(Vertex[] verticesFront, Vertex[] verticesBack, Resource[] centralResources){
        super(verticesFront);
        this.verticesBack = verticesBack;
        this.centralResources = centralResources;

    }
}


