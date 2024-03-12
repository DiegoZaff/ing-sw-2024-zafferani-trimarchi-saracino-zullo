package it.polimi.ingsw.gc28.model.cards;

import it.polimi.ingsw.gc28.model.Vertex;

public abstract class CardGame extends Card {
    private Vertex[] verticesFront;
    public CardGame(Vertex[] verticesFront){
        this.verticesFront = verticesFront;
    }
}

