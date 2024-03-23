package it.polimi.ingsw.gc28.model;
import it.polimi.ingsw.gc28.model.cards.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;
import java.util.Optional;

import it.polimi.ingsw.gc28.model.challenge.Challenge;
import it.polimi.ingsw.gc28.model.challenge.utils.ChallengeType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourcePrimaryType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceSpecialType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Class that represents the deck of cards.
 */
public class Deck {
    private final ArrayList<CardResource> cardResourceDeck = new ArrayList<CardResource>();
    private final ArrayList<CardGold> cardGoldDeck = new ArrayList<CardGold>();
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
     *
     * //TODO : change and make a next() method for each type of deck
     */
    public Optional<Card> next() {
        // ! to be implemented.
        return Optional.empty();

    };

    /**
      This constructor generates a deck of cards
     */
    public Deck() throws IOException {

        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader(".//src//main//java//it.polimi.ingsw.gc28//json//Card.json");

        Object obj;
        try {
            obj = jsonParser.parse(reader);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        JSONObject card = (JSONObject) obj;
        JSONArray deckResources =(JSONArray)card.get("CardResource");

        for(int i = 0; i< deckResources.size(); i++){
            JSONObject cardResource = (JSONObject) deckResources.get(i);
            ResourceType[] resourceCard = new ResourceType[4];

            resourceCard[0] = (ResourceType) cardResource.get("vertexOne");
            resourceCard[1] = (ResourceType) cardResource.get("vertexTwo");
            resourceCard[2] = (ResourceType) cardResource.get("vertexThree");
            resourceCard[3] = (ResourceType) cardResource.get("vertexFour");
            ResourcePrimaryType resourcePrimary = (ResourcePrimaryType) cardResource.get("resourcePrimary");
            int pointsPerPlay = (int) cardResource.get("pointsPerPlay");

            cardResourceDeck.add(new CardResource(resourceCard, resourcePrimary, pointsPerPlay));
        }

        JSONArray deckGold = (JSONArray)card.get("CardGold");

        for(int i = 0; i< deckGold.size(); i++) {
            JSONObject cardGold = (JSONObject) deckResources.get(i);

            ResourceType[] resourceCard = new ResourceType[4];
            resourceCard[0] = (ResourceType) cardGold.get("vertexOne");
            resourceCard[1] = (ResourceType) cardGold.get("vertexTwo");
            resourceCard[2] = (ResourceType) cardGold.get("vertexThree");
            resourceCard[3] = (ResourceType) cardGold.get("vertexFour");
            ResourcePrimaryType resourcePrimary = (ResourcePrimaryType) cardGold.get("resourcePrimary");
            int pointsPerPlay = (int) cardGold.get("pointsPerPlay");
            ResourcePrimaryType[] resourceNeeded = new ResourcePrimaryType[5];
            resourceNeeded[0] = (ResourcePrimaryType) cardGold.get("resourceNeededOne");
            resourceNeeded[1] = (ResourcePrimaryType) cardGold.get("resourceNeededTwo");
            resourceNeeded[2] = (ResourcePrimaryType) cardGold.get("resourceNeededThree");
            resourceNeeded[3] = (ResourcePrimaryType) cardGold.get("resourceNeededFour");
            resourceNeeded[4] = (ResourcePrimaryType) cardGold.get("resourceNeededFive");
            ChallengeType challenge= (ChallengeType) cardGold.get("challenge");
            ResourceSpecialType resourceChallenge = (ResourceSpecialType) cardGold.get("resourceChallenge");

            cardGoldDeck.add(new CardGold(resourceCard, resourcePrimary, pointsPerPlay, resourceNeeded, challenge, resourceChallenge));
        }

    };
}
