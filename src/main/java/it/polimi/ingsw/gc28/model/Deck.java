package it.polimi.ingsw.gc28.model;
import it.polimi.ingsw.gc28.model.cards.*;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Class that represents the deck of cards.
 */
public class Deck {
    private final ArrayList<CardResource> cardResource = new ArrayList<CardResource>();
    private final ArrayList<CardGold> cardGold = new ArrayList<CardGold>();
    private final ArrayList<CardInitial> cardInitial = new ArrayList<CardInitial>();
    private final ArrayList<CardObjective> cardObjective = new ArrayList<CardObjective>();

    /**
     * This method shuffles the deck randomly.
     */
    public void shuffle(ArrayList<CardResource> cardResource, ArrayList<CardGold> cardGold,
                        ArrayList<CardInitial> cardInitial, ArrayList<CardObjective> cardObjective){
        // ! to be implemented.
    };

    /**
     * This method is responsible for drawing the card.
     * @return the card on top of the deck, if the array is not empty, removing it from the array.
     * Otherwise, it returns null.
     */
    public Optional<Card> next() {
        // ! to be implemented.
        return Optional.empty();

    };

    /**
     * This constructor generates a deck of cards
     */
    public Deck(Class<?> type){
        if(type == CardInitial.class){
            // initialize deck of initial cards...
        }else if (type == CardObjective.class){
            // initialize deck of objective cards...
        } else if (type == CardGold.class){
            // initialize deck of gold cards...
        } else if(type == CardResource.class){
            // initialize deck of resource cards...
        }else{
            //throw error
        }
    };
}
