package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.cards.*;
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
    private final ArrayList<CardResource> cardResourceDeck = new ArrayList<CardResource>();
    private final ArrayList<CardGold> cardGoldDeck = new ArrayList<CardGold>();
    private final ArrayList<CardInitial> cardInitialDeck = new ArrayList<CardInitial>();
    private final ArrayList<CardObjective> cardObjectiveDeck = new ArrayList<CardObjective>();

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
                String[] resourceCard = new String[4];
                ResourceType[] resources = new ResourceType[4];

                resourceCard[0] = (String) cardResource.get("vertexOne");
                resourceCard[1] = (String) cardResource.get("vertexTwo");
                resourceCard[2] = (String) cardResource.get("vertexThree");
                resourceCard[3] = (String) cardResource.get("vertexFour");
                String resourcePrimary = (String) cardResource.get("resourcePrimary");
                String pointsPerPlay = (String) cardResource.get("pointsPerPlay");

                StringToResourceType(resourceCard, resources);
                ResourcePrimaryType resPrimary = ResourcePrimaryType.valueOf(resourcePrimary);
                int points = Integer.parseInt(pointsPerPlay);

                String id = (String) cardResource.get("id");

                cardResourceDeck.add(new CardResource(id, resources, resPrimary, points));
            }

            JSONArray deckGold = (JSONArray)card.get("CardGold");
            for(int i = 0; i< deckGold.size(); i++) {
                JSONObject cardGold = (JSONObject) deckGold.get(i);
                String[] resourceCard = new String[4];
                String[] resourceNeeded = new String[5];
                ResourceType[] resources = new ResourceType[4];
                ResourcePrimaryType[] resNeeded = new ResourcePrimaryType[5];
                ChallengeType cha;
                ResourceSpecialType resChallenge;

                resourceCard[0] = (String) cardGold.get("vertexOne");
                resourceCard[1] = (String) cardGold.get("vertexTwo");
                resourceCard[2] = (String) cardGold.get("vertexThree");
                resourceCard[3] = (String) cardGold.get("vertexFour");
                String resourcePrimary = (String) cardGold.get("resourcePrimary");
                String pointsPerPlay = (String) cardGold.get("pointsPerPlay");
                resourceNeeded[0] = (String) cardGold.get("resourceNeededOne");
                resourceNeeded[1] = (String) cardGold.get("resourceNeededTwo");
                resourceNeeded[2] = (String) cardGold.get("resourceNeededThree");
                resourceNeeded[3] = (String) cardGold.get("resourceNeededFour");
                resourceNeeded[4] = (String) cardGold.get("resourceNeededFive");
                String challenge= (String) cardGold.get("challenge");
                String resourceChallenge = (String) cardGold.get("resourceChallenge");

                StringToResourceType(resourceCard, resources);
                ResourcePrimaryType resPrimary = ResourcePrimaryType.valueOf(resourcePrimary);
                int points = Integer.parseInt(pointsPerPlay);
                StringToResourcePrimaryType(resourceNeeded, resNeeded);
                if(challenge!= null){
                    cha = ChallengeType.valueOf(challenge);
                }
                else{
                    cha = null;
                }
                if(resourceChallenge!= null){
                    resChallenge = ResourceSpecialType.valueOf(resourceChallenge);
                }
                else{
                    resChallenge = null;
                }

                String id = (String) cardGold.get("id");

                cardGoldDeck.add(new CardGold(id, resources, resPrimary, points, resNeeded, cha, resChallenge));
            }

            JSONArray deckInitial = (JSONArray)card.get("CardInitial");
            for(int i = 0; i<deckInitial.size(); i++){
                JSONObject cardInitial = (JSONObject) deckInitial.get(i);
                String[] resourceBack = new String[4];
                String[] resourceFront = new String[4];
                String[] resourceCenter = new String[3];

                ResourceType[] resBack = new ResourceType[4];
                ResourceType[] resFront = new ResourceType[4];
                ResourceType[] resCenter = new ResourceType[3];

                resourceBack[0] = (String) cardInitial.get("vertexBackOne");
                resourceBack[1] = (String) cardInitial.get("vertexBackTwo");
                resourceBack[2] = (String) cardInitial.get("vertexBackThree");
                resourceBack[3] = (String) cardInitial.get("vertexBackFour");
                resourceFront[0] = (String) cardInitial.get("vertexFrontOne");
                resourceFront[1] = (String) cardInitial.get("vertexFrontTwo");
                resourceFront[2] = (String) cardInitial.get("vertexFrontThree");
                resourceFront[3] = (String) cardInitial.get("vertexFrontFour");
                resourceCenter[0] = (String) cardInitial.get("centralResourceOne");
                resourceCenter[1] = (String) cardInitial.get("centralResourceTwo");
                resourceCenter[2] = (String) cardInitial.get("centralResourceThree");

                StringToResourceType(resourceBack, resBack);
                StringToResourceType(resourceFront, resFront);
                StringToResourceType(resourceCenter, resCenter);

                String id = (String) cardInitial.get("id");

                cardInitialDeck.add(new CardInitial(id, resBack, resFront, resCenter));

            }

            JSONArray deckObjective = (JSONArray)card.get("CardObjective");
            int j = 2;
            for(int i = 0; i< deckObjective.size()/j; i++) {
                JSONObject cardObjective = (JSONObject) deckObjective.get(i);
                String[] resourceNeeded = new String[3];
                ResourceType[] resNeeded = new ResourceType[3];

                String pointsCard = (String) cardObjective.get("points");
                resourceNeeded[0] = (String) cardObjective.get("resourceNeededOne");
                resourceNeeded[1] = (String) cardObjective.get("resourceNeededTwo");
                resourceNeeded[2] = (String) cardObjective.get("resourceNeededThree");

                int points = Integer.parseInt(pointsCard);
                StringToResourceType(resourceNeeded, resNeeded);

                String id = (String) cardObjective.get("id");

                cardObjectiveDeck.add(new CardObjective(id, points, resNeeded));
            }
            for(int i = deckObjective.size()/j; i<deckObjective.size(); i++) {
                JSONObject cardObjective = (JSONObject) deckObjective.get(i);
                String[] resourcePosition = new String[3];
                ResourcePrimaryType[] resPosition = new ResourcePrimaryType[3];

                String pointsCard = (String) cardObjective.get("points");
                String positionType = (String) cardObjective.get("positionType");
                resourcePosition[0] = (String) cardObjective.get("resourcePrimaryOne");
                resourcePosition[1] = (String) cardObjective.get("resourcePrimaryTwo");
                resourcePosition[2] = (String) cardObjective.get("resourcePrimaryThree");

                int points = Integer.parseInt(pointsCard);
                GeneralPositionType posType = GeneralPositionType.valueOf(positionType);
                StringToResourcePrimaryType(resourcePosition, resPosition);

                String id = (String) cardObjective.get("id");

                cardObjectiveDeck.add(new CardObjective(id, posType, points, resPosition));
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is used for testing purposes to make decks with known ordered cards
     * due to applied permutations.
     */
    public Deck(ArrayList<String> deckCardResourcesPermutations,
                ArrayList<String> deckCardGoldPermutations,
                ArrayList<String> deckCardInitialPermutations,
                ArrayList<String> deckCardObjectivePermutations) throws  IOException{
        this();
        this.mixFromPermutation(cardInitialDeck, deckCardInitialPermutations);
        this.mixFromPermutation(cardGoldDeck, deckCardGoldPermutations);
        this.mixFromPermutation(cardObjectiveDeck, deckCardObjectivePermutations);
        this.mixFromPermutation(cardResourceDeck, deckCardResourcesPermutations);
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

    public void StringToResourceType(String[] str, ResourceType[] res){
        for(int a=0; a<res.length;a++){
            if(str[a]!= null){
                res[a] = ResourceType.valueOf(str[a]);
            }
            else{
                res[a]=null;
            }
        }
    }
    public void StringToResourcePrimaryType(String[] str, ResourcePrimaryType[] res){
        for(int a=0; a<res.length;a++){
            if(str[a]!= null){
                res[a] = ResourcePrimaryType.valueOf(str[a]);
            }
            else{
                res[a]=null;
            }
        }
    }
}
