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

    public ArrayList<CardGold> deckCardGold = new ArrayList<>();

    public ArrayList<CardObjective> deckCardObjective = new ArrayList<>();


    public TestingDeck(ArrayList<Integer> deckCardResourcesPermutations, ArrayList<Integer> deckCardGoldPermutations, ArrayList<Integer> deckCardInitialPermutations, ArrayList<Integer> deckCardObjectivePermutations) throws Exception {
        super(deckCardResourcesPermutations, deckCardGoldPermutations, deckCardInitialPermutations, deckCardObjectivePermutations);

        for(int i = 0; i < deckCardInitialPermutations.size(); i++){
            Optional<CardInitial> card = super.nextInitial();

            if(card.isEmpty()){
                throw new Exception();
            }

            deckCardInitials.add(card.get());
        }

        for(int i = 0; i < deckCardResourcesPermutations.size(); i++){
            Optional<CardResource> card = super.nextResource();

            if(card.isEmpty()){
                throw new Exception();
            }

            deckCardResource.add(card.get());
        }

        for(int i = 0; i < deckCardGoldPermutations.size(); i++){
            Optional<CardGold> card = super.nextGold();

            if(card.isEmpty()){
                throw new Exception();
            }

            deckCardGold.add(card.get());
        }

        for(int i = 0; i < deckCardObjectivePermutations.size(); i++){
            Optional<CardObjective> card = super.nextObjective();

            if(card.isEmpty()){
                throw new Exception();
            }

            deckCardObjective.add(card.get());
        }
    }


}
