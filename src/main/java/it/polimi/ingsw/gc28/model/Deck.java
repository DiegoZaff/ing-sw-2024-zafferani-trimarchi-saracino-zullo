package it.polimi.ingsw.gc28.model;
import it.polimi.ingsw.gc28.model.cards.*;

import java.util.Random;
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
     * This method shuffles each type of deck randomly.
     */
    public void shuffle(ArrayList<Card> card){

        Random random = new Random();
        Card tmp;
        for(int i=card.size()-1; i>0; i--){
            int j= random.nextInt(card.size()+1);
            tmp = card.get(i);
            card.set(i, card.get(j));
            card.set(j, tmp);
        }
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

    /*
      This constructor generates a deck of cards
     */
    public Deck(){
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
