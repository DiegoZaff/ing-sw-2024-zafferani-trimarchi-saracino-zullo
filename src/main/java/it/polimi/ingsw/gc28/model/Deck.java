package it.polimi.ingsw.gc28.model;
import it.polimi.ingsw.gc28.model.cards.*;
import it.polimi.ingsw.gc28.model.resources.Resource;
import it.polimi.ingsw.gc28.model.resources.ResourcePrimary;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;
import java.util.Optional;

import it.polimi.ingsw.gc28.model.resources.utils.ResourcePrimaryType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static java.lang.Integer.parseInt;


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
        int j;
        for(int i=card.size()-1; i>0; i--){
            j= random.nextInt(card.size()+1);
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
    public Deck() throws IOException, ParseException {

        ResourceType[] resourceType;
        Resource[] resource;
        Vertex[] vertex;
        ResourcePrimary resourcePrimary;
        int pointsPerPlay;

        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader(".//src//main//java//it.polimi.ingsw.gc28//json//Card.json");

        Object obj;
        try {
            obj = jsonParser.parse(reader);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        JSONObject CardResource = (JSONObject) obj;
        JSONArray DeckResources =(JSONArray)CardResource.get("CardResources");

        for (Object deckResource : DeckResources) {
            JSONObject Card = (JSONObject) deckResource;

            // to be implemented!
        }
    };
}
