package it.polimi.ingsw.gc28.model;

public abstract class CarteDaGioco extends Carta {
    private Vertice[] verticiFronte;
    public CarteDaGioco(Vertice[] verticiFronte){
        this.verticiFronte = verticiFronte;
    }
}

