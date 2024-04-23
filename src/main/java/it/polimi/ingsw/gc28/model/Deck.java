package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.cards.*;
import it.polimi.ingsw.gc28.model.cards.utils.ParsingHelper;
import it.polimi.ingsw.gc28.model.challenge.utils.ChallengeType;
import it.polimi.ingsw.gc28.model.objectives.positions.utils.GeneralPositionType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourcePrimaryType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceSpecialType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

/**
 * Class that represents the deck of cards.
 */
public class Deck {
    private final ArrayList<CardResource> cardResourceDeck;
    private final ArrayList<CardGold> cardGoldDeck;
    private final ArrayList<CardInitial> cardInitialDeck;
    private final ArrayList<CardObjective> cardObjectiveDeck;

    /**
     * This method shuffles each type of deck randomly.
     */
    public <T extends Card> void shuffle(ArrayList<T > card){
        Random random = new Random();
        T tmp;
        int j;
        for(int i=card.size()-1; i>0; i--){
            j= random.nextInt(card.size());
            tmp = card.get(i);
            card.set(i, card.get(j));
            card.set(j, tmp);
        }
    }

    public void shuffleAll(){
       shuffle(this.cardResourceDeck);
       shuffle((this.cardGoldDeck));
       shuffle(this.cardObjectiveDeck);
       shuffle(this.cardInitialDeck);
    }

    /**
     * This method is responsible for drawing the card.
     * @return the card on top of the card resource deck, if the array is not empty, removing it from the array.
     * Otherwise, it returns null.
     */
    public Optional<CardResource> nextResource() {
        if(cardResourceDeck.isEmpty()){
            return Optional.empty();
        }
        return Optional.ofNullable(cardResourceDeck.removeFirst());
    }

    /**
     * This method is responsible for drawing the card.
     * @return the card on top of the card gold deck, if the array is not empty, removing it from the array.
     * Otherwise, it returns null.
     * */
    public Optional<CardGold> nextGold() {
        if(cardGoldDeck.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(cardGoldDeck.removeFirst());
    }

    /**
     * This method is responsible for drawing the card.
     * @return the card on top of the card objective deck, if the array is not empty, removing it from the array.
     * Otherwise, it returns null.
     */
    public Optional<CardObjective> nextObjective() {
        if(cardObjectiveDeck.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(cardObjectiveDeck.removeFirst());
    }

    /**
     * This method is responsible for drawing the card.
     * @return the card on top of the card initial deck, if the array is not empty, removing it from the array.
     * Otherwise, it returns null.
     */
    public Optional<CardInitial> nextInitial() {
        if(cardInitialDeck.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(cardInitialDeck.removeFirst());
    }

    /**
      This constructor generates a deck of cards
     */
    public Deck() throws IOException {
        cardResourceDeck = new ArrayList<>();
        cardGoldDeck = new ArrayList<>();
        cardInitialDeck = new ArrayList<>();
        cardObjectiveDeck = new ArrayList<>();

        JSONParser jsonParser = new JSONParser();
        String path = "./src/main/java/it/polimi/ingsw/gc28/Cards.json";
        FileReader reader = new FileReader(path);

        Object obj;
        try {
            obj = jsonParser.parse(reader);
            JSONObject card = (JSONObject) obj;

            JSONArray deckResources =(JSONArray)card.get("CardResource");
            for(int i = 0; i<deckResources.size(); i++){
                JSONObject cardResource = (JSONObject) deckResources.get(i);

                CardResource cardI = ParsingHelper.parseCardResource(cardResource);

                cardResourceDeck.add(cardI);
            }

            JSONArray deckGold = (JSONArray)card.get("CardGold");
            for(int i = 0; i< deckGold.size(); i++) {
                JSONObject cardGold = (JSONObject) deckGold.get(i);

                CardGold cardI = ParsingHelper.parseGoldCard(cardGold);

                cardGoldDeck.add(cardI);
            }

            JSONArray deckInitial = (JSONArray)card.get("CardInitial");
            for(int i = 0; i<deckInitial.size(); i++){
                JSONObject cardInitial = (JSONObject) deckInitial.get(i);

                CardInitial cardI = ParsingHelper.parseCardInitial(cardInitial);

                cardInitialDeck.add(cardI);
            }

            JSONArray deckObjective = (JSONArray)card.get("CardObjective");
            int j = 2;
            for(int i = 0; i< deckObjective.size()/j; i++) {
                JSONObject cardObjective = (JSONObject) deckObjective.get(i);

                CardObjective cardI = ParsingHelper.parseObjectiveResources(cardObjective);

                cardObjectiveDeck.add(cardI);
            }
            for(int i = deckObjective.size()/j; i<deckObjective.size(); i++) {
                JSONObject cardObjective = (JSONObject) deckObjective.get(i);

                CardObjective cardI = ParsingHelper.parseObjectivePositional(cardObjective);

                cardObjectiveDeck.add(cardI);
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is used for testing purposes to make decks with known ordered cards
     * due to applied permutations.
     */
    public Deck(ArrayList<CardResource> deckCardResources,
                ArrayList<CardGold> deckCardGold,
                ArrayList<CardInitial> deckCardInitial,
                ArrayList<CardObjective> deckCardObjective) throws  IOException{
        this.cardResourceDeck = deckCardResources;
        this.cardGoldDeck = deckCardGold;
        this.cardInitialDeck = deckCardInitial;
        this.cardObjectiveDeck = deckCardObjective;
    }

    /**
     * This method applies a permutation to the deck of cards.
     * Used only for testing purposes inside the special constructor.
     */
    /*private <T> void mixFromPermutation(ArrayList<T> deck, ArrayList<String> deck) throws  IllegalArgumentException{
        if (deck.size() != permutation.size()){
            throw new IllegalArgumentException();
        }

        ArrayList<T> newDeck = new ArrayList<>(deck);

        for(int i = 0; i < deck.size(); i++){
            int nextId = deck.;
            newDeck.set(nextIndex, deck.get(i));
        }

        for (int i = 0; i < deck.size(); i++) {
            int nextIndex = permutation.get(i);
            deck.set(nextIndex, newDeck.get(i));
        }
    }*/

    public CardGold getNextGoldCard(){
        return cardGoldDeck.getFirst();
    }

    public CardResource getNextResourceCard(){
        return cardResourceDeck.getFirst();
    }


}
