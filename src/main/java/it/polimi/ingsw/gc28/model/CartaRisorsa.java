package it.polimi.ingsw.gc28.model;

public class CartaRisorsa extends CarteDaGioco{
    private RisorsaPrimaria risorsa;
    private int puntiSuGiocata;
    public CartaRisorsa (Vertice[] verticiFronte, RisorsaPrimaria risorsa, int puntiSuGiocata){
        super(verticiFronte);
        this.puntiSuGiocata = puntiSuGiocata;
        this.risorsa = risorsa;

    }

}

