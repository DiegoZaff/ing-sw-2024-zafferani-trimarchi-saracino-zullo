package it.polimi.ingsw.gc28.games;

import it.polimi.ingsw.gc28.model.Deck;
import it.polimi.ingsw.gc28.model.cards.CardGold;
import it.polimi.ingsw.gc28.model.cards.CardInitial;
import it.polimi.ingsw.gc28.model.cards.CardObjective;
import it.polimi.ingsw.gc28.model.cards.CardResource;

import java.util.ArrayList;
import java.util.Optional;

public class TestingDeck extends Deck {

    public ArrayList<CardInitial> deckCardInitials = new ArrayList<>();

    public ArrayList<CardResource> deckCardResource = new ArrayList<>();

    public ArrayList<CardGold> deckCardGolds = new ArrayList<>();

    public ArrayList<CardObjective> deckCardObjectives = new ArrayList<>();


    public TestingDeck(ArrayList<String> deckCardResources, ArrayList<String> deckCardGold, ArrayList<String> deckCardInitial, ArrayList<String> deckCardObjective) throws Exception {
        super(deckCardResources, deckCardGold, deckCardInitial, deckCardObjective);

        for(int i = 0; i < deckCardInitial.size(); i++){
            Optional<CardInitial> card = super.nextInitial();

            if(card.isEmpty()){
                throw new Exception();
            }

            deckCardInitials.add(card.get());
        }

        for(int i = 0; i < deckCardResources.size(); i++){
            Optional<CardResource> card = super.nextResource();

            if(card.isEmpty()){
                throw new Exception();
            }

            deckCardResource.add(card.get());
        }

        for(int i = 0; i < deckCardGold.size(); i++){
            Optional<CardGold> card = super.nextGold();

            if(card.isEmpty()){
                throw new Exception();
            }

            deckCardGolds.add(card.get());
        }

        for(int i = 0; i < deckCardObjective.size(); i++){
            Optional<CardObjective> card = super.nextObjective();

            if(card.isEmpty()){
                throw new Exception();
            }

            deckCardObjectives.add(card.get());
        }
    }

    public Optional<CardResource> getCardResFromId(String cardId){
        Optional<CardResource> card = deckCardResource.stream()
                .filter(cardRes -> cardRes.getId().equals(cardId))
                .findFirst();
        return card;
    }

    public Optional<CardGold> getCardGoldFromId(String cardId){
        Optional<CardGold> card = deckCardGolds.stream()
                .filter(cardGold -> cardGold.getId().equals(cardId))
                .findFirst();
        return card;
    }

    public Optional<CardInitial> getCardInitialFromId(String cardId){
        Optional<CardInitial> card = deckCardInitials.stream()
                .filter(cardInitial -> cardInitial.getId().equals(cardId))
                .findFirst();
        return card;
    }

    public Optional<CardObjective> getCardObjectiveFromId(String cardId){
        Optional<CardObjective> card = deckCardObjectives.stream()
                .filter(cardObjective -> cardObjective.getId().equals(cardId))
                .findFirst();
        return card;
    }



}
