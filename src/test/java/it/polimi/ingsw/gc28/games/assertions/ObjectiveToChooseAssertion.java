package it.polimi.ingsw.gc28.games.assertions;

import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.cards.Card;
import it.polimi.ingsw.gc28.model.cards.CardObjective;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class ObjectiveToChooseAssertion extends GameAssertion {

    private final String playerNick;

    private ArrayList<String> actualObjsToChoose;
    private final ArrayList<String> expectedObjsToChoose;


    public ObjectiveToChooseAssertion(String card1, String card2, String playerNick) {
        this.playerNick = playerNick;
        this.actualObjsToChoose = new ArrayList<>();
        this.expectedObjsToChoose = new ArrayList<>();
        this.expectedObjsToChoose.add(card1);
        this.expectedObjsToChoose.add(card2);
    }

    @Override
    public boolean verifyAssertion(Game game) {

        Optional<Player> player = super.getPlayerFromNick(playerNick, game);

        if(player.isEmpty()){
            return false;
        }

        Optional<ArrayList<CardObjective>> objsToChoose = player.get().getObjectivesToChoose();

        objsToChoose.ifPresent(cardObjectives -> actualObjsToChoose = cardObjectives.stream().map((Card::getId)).collect(Collectors.toCollection(ArrayList::new)));

        if(expectedObjsToChoose.size() != actualObjsToChoose.size()){
            return false;
        }

        //check all expected ids are presents
        for(String id1 : expectedObjsToChoose){
            boolean found = false;
            for(String id2 : actualObjsToChoose){
                if(id2.equals(id1)){
                    found = true;
                    break;
                }
            }
            if(!found){
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {

        String expectedCardIdsString = String.join(", ", expectedObjsToChoose);

        String actualCardIdsString = String.join(", ", actualObjsToChoose);

        return String.format("HandOfPlayerAssertion --- expected: %s  actual: %s", expectedCardIdsString, actualCardIdsString);    }
}
