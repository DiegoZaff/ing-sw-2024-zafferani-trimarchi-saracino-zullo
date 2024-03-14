package it.polimi.ingsw.gc28.model;
import it.polimi.ingsw.gc28.model.cards.*;

import java.io.FileNotFoundException;
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
     * This method shuffles the deck randomly.
     */
    public void shuffle(ArrayList<CardResource> cardResource, ArrayList<CardGold> cardGold,
                        ArrayList<CardInitial> cardInitial, ArrayList<CardObjective> cardObjective){

        Random random = new Random();
        for(int i=cardResource.size()-1; i>0; i--){
            int j= random.nextInt(i+1);
            CardResource tmp = cardResource.get(i);
            cardResource.set(i, cardResource.get(j));
            cardResource.set(i, tmp);
        }

        for(int i=cardGold.size()-1; i>0; i--){
            int j= random.nextInt(i+1);
            CardGold tmp = cardGold.get(i);
            cardGold.set(i, cardGold.get(j));
            cardGold.set(i, tmp);
        }

        for(int i=cardInitial.size()-1; i>0; i--){
            int j= random.nextInt(i+1);
            CardInitial tmp = cardInitial.get(i);
            cardInitial.set(i, cardInitial.get(j));
            cardInitial.set(i, tmp);
        }

        for(int i=cardObjective.size()-1; i>0; i--){
            int j= random.nextInt(i+1);
            CardObjective tmp = cardObjective.get(i);
            cardObjective.set(i, cardObjective.get(j));
            cardObjective.set(i, tmp);
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
