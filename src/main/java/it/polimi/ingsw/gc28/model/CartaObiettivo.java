package it.polimi.ingsw.gc28.model;

public class CartaObiettivo extends Carta {

    private final tipoCartaObiettivo tipo;
    enum tipoCartaObiettivo{
        NumeroRisorse,
        PosizioneCarte
    }
    private final int punti;

}
