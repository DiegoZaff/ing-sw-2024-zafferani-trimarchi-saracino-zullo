package it.polimi.ingsw.gc28;

class CartaIniziale extends CarteDaGioco{
    private Vertice[] verticiRetro;
    private Risorsa[] risorseCentrali;
    public CartaIniziale (Vertice[] verticiFronte, Vertice[] verticiRetro, Risorsa[] risorseCentrali){
        super(verticiFronte);
        this.verticiRetro = verticiRetro;
        this.risorseCentrali = risorseCentrali;

    }
}


