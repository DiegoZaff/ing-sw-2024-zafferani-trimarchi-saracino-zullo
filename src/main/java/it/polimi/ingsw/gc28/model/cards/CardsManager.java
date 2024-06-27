package it.polimi.ingsw.gc28.model.cards;


import it.polimi.ingsw.gc28.model.cards.utils.ParsingHelper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class CardsManager {

    private static CardsManager instance;

    private final Map<String, CardResource> cardResourceMap;

    private final Map<String, CardGold> cardGoldMap;

    private final Map<String, CardObjective> cardObjectiveMap;

    private final Map<String, CardInitial> cardInitialMap;

    /**
     * this method is used for the initialization of every card by reading a JSON file
     */
    private CardsManager()  {
        cardResourceMap = new HashMap<>();
        cardGoldMap = new HashMap<>();
        cardInitialMap = new HashMap<>();
        cardObjectiveMap = new HashMap<>();

        JSONParser jsonParser = new JSONParser();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("Cards.json");

        if(inputStream == null){
            throw new RuntimeException("Could not read Cards.json");
        }

        InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(inputStream));
        Object obj;
        try {
            obj = jsonParser.parse(reader);
            JSONObject card = (JSONObject) obj;

            JSONArray deckResources =(JSONArray)card.get("CardResource");
            for(int i = 0; i<deckResources.size(); i++){
                JSONObject cardResource = (JSONObject) deckResources.get(i);

                CardResource cardI = ParsingHelper.parseCardResource(cardResource);

                cardResourceMap.put(cardI.getId(),cardI);
            }

            JSONArray deckGold = (JSONArray)card.get("CardGold");
            for(int i = 0; i< deckGold.size(); i++) {
                JSONObject cardGold = (JSONObject) deckGold.get(i);

                CardGold cardI = ParsingHelper.parseGoldCard(cardGold);

                cardGoldMap.put(cardI.getId(),cardI);
            }

            JSONArray deckInitial = (JSONArray)card.get("CardInitial");
            for(int i = 0; i<deckInitial.size(); i++){
                JSONObject cardInitial = (JSONObject) deckInitial.get(i);

                CardInitial cardI = ParsingHelper.parseCardInitial(cardInitial);

                cardInitialMap.put(cardI.getId(),cardI);
            }

            JSONArray deckObjective = (JSONArray)card.get("CardObjective");
            int j = 2;
            for(int i = 0; i< deckObjective.size()/j; i++) {
                JSONObject cardObjective = (JSONObject) deckObjective.get(i);

                CardObjective cardI = ParsingHelper.parseObjectiveResources(cardObjective);

                cardObjectiveMap.put(cardI.getId(),cardI);
            }
            for(int i = deckObjective.size()/j; i<deckObjective.size(); i++) {
                JSONObject cardObjective = (JSONObject) deckObjective.get(i);

                CardObjective cardI = ParsingHelper.parseObjectivePositional(cardObjective);

                cardObjectiveMap.put(cardI.getId(),cardI);
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }


    public static CardsManager getInstance() {
        if (instance == null) {
            instance = new CardsManager();
        }
        return instance;
    }


    public Optional<? extends  Card> getCardFromId(String id){
        if (id.startsWith("OBJ")){
            return getCardObjectiveFromId(id);
        }else{
            return getCardGameFromId(id);
        }
    }

    public Optional<? extends CardGame> getCardGameFromId(String id){
        if(id.startsWith("INI")){
            return getCardInitialFromId(id);
        }else{
            return getCardResourceFromId(id);
        }
    }

    public Optional<? extends CardResource> getCardResourceFromId(String id){
        if(id.startsWith("RES")){
            return Optional.ofNullable(cardResourceMap.get(id));
        }
        return getCardGoldFromId(id);
    }


    public Optional<CardGold> getCardGoldFromId(String id){
        return Optional.ofNullable(cardGoldMap.get(id));
    }

    public Optional<CardInitial> getCardInitialFromId(String id){
        return Optional.ofNullable(cardInitialMap.get(id));
    }

    public Optional<CardObjective> getCardObjectiveFromId(String id){
        return Optional.ofNullable(cardObjectiveMap.get(id));    }

}
