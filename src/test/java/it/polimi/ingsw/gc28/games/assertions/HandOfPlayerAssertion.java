package it.polimi.ingsw.gc28.games.assertions;

import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.cards.CardResource;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class HandOfPlayerAssertion extends GameAssertion{
    private ArrayList<CardResource> hand;
    private final ArrayList<String> expectedCardIds;

    private final String playerName;

    public HandOfPlayerAssertion(String card1, String card2, String card3, String playerName) {
        this.expectedCardIds = new ArrayList<>();
        if(card1 != null){
            expectedCardIds.add(card1);
        }
        if(card2 != null){
            expectedCardIds.add(card2);
        }
        if(card3 != null){
            expectedCardIds.add(card3);
        }
        this.playerName = playerName;
    }

    @Override
    public boolean verifyAssertion(Game game) {

        Optional<Player> player = super.getPlayerFromNick(playerName, game);

        if(player.isEmpty()){
            return false;
        }

        hand = player.get().gethand();

        if(hand.size() != expectedCardIds.stream().filter(Objects::nonNull).toList().size()){
            return false;
        }

        for(String id : expectedCardIds){
            boolean found = false;
            for(CardResource c2 : hand){
                if(c2.getId().equals(id)){
                    found = true;
                    break;
                }
            }
            if(!found){
                return false;
            }
        }

        //all cards found
        return true;
    }

    @Override
    public String toString() {

        String expectedCardIdsString = String.join(", ", expectedCardIds);

        String actualCardIdsString = hand.stream().map((Object::toString)).collect(Collectors.joining(", "));


        return String.format("HandOfPlayerAssertion --- expected: %s,  actual: %s", expectedCardIdsString, actualCardIdsString);
    }
}
