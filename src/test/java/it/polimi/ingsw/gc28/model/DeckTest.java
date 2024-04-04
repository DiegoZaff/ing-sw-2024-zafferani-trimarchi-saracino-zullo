package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.cards.CardGold;
import it.polimi.ingsw.gc28.model.cards.CardInitial;
import it.polimi.ingsw.gc28.model.cards.CardObjective;
import it.polimi.ingsw.gc28.model.cards.CardResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {

    private Deck deck;


    /**
     * Initialize deck before each test
     */
    @BeforeEach
    public void initDeck(){
        try{
            deck = new Deck();
            assertTrue(true, "deck initialized correctly");
        }catch (IOException e){
            fail("Failed to initialize deck: " + e.getMessage());
        }catch (Exception e){
            fail("Exception: " + e.getMessage());
        }
    }
    /**
     * This tests that the card is drawn correctly from each deck
     */
    @Test
    public void drawCards(){

        Optional<CardObjective> cardObj = deck.nextObjective();

        assertTrue(cardObj.isPresent(), "card objective drawn");

        Optional<CardGold> cardGold = deck.nextGold();

        assertTrue(cardGold.isPresent(), "card gold drawn");

        Optional<CardInitial> cardInitial = deck.nextInitial();

        assertTrue(cardInitial.isPresent(), "card initial drawn");

        Optional<CardResource> cardResource = deck.nextResource();

        assertTrue(cardResource.isPresent(), "card resource drawn");
    }


    /**
     * Check that all resource cards are drawn correctly and removed from the deck.
     */
    @Test
    public void drawAllCardsResources(){
        for(int i = 0; i < 41; i++){
            Optional<CardResource> card = deck.nextResource();
            if(i < 40){
                assertTrue(card.isPresent(), String.format("%d° card is present", i));
            }else{
                assertTrue(card.isEmpty(), "41° card is not present");
            }
        }
    }
}
