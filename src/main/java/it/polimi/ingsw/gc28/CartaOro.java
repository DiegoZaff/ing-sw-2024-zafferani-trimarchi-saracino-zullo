package it.polimi.ingsw.gc28;

public class CartaOro extends CartaRisorsa {
    private Map<Risorsa, Integer> giocabilità;
    private Challenge tipoDìChallenge;

    public CartaOro(Vertice[] verticiFronte, RisorsaPrimaria risorsa, int puntiSuGiocata, Map<Risorsa, Integer> giocabilità, Challenge tipoDìChallenge){
        super(verticiFronte, risorsa, puntiSuGiocata);
        this.giocabilità = giocabilità;
        this.tipoDìChallenge = tipoDìChallenge;
    }
}