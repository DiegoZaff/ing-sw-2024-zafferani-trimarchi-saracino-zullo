package it.polimi.ingsw.gc28.model;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Class that represents the deck of cards.
 */
public class Mazzo {
    private ArrayList<Carta>[] carte;

    /**
     * This method shuffles the deck randomly.
     */
    public void shuffle(){
        // ! to be implemented.
    };

    /**
     * This method is responsible for drawing the card.
     * @return the card on top of the deck, if the array is not empty, removing it from the array.
     * Otherwise, it returns null.
     */
    public Optional<Carta> next() {
        // ! to be implemented.
        return Optional.empty();

    };

    /**
     * This constructor generates a deck of cards
     */
    public Mazzo(Class<?> type){
        if(type == CartaIniziale.class){
            // initialize deck of initial cards...
        }else if (type == CartaObiettivo.class){
            // initialize deck of objective cards...
        } else if (type == CartaOro.class){
            // initialize deck of gold cards...
        } else if(type == CartaRisorsa.class){
            // initialize deck of resource cards...
        }else{
            //throw error
        }
    };
}
