package it.polimi.ingsw.gc28.games.assertions;

import it.polimi.ingsw.gc28.model.Game;

import java.util.Objects;

public class GlobalObjectivesGameAssertion extends GameAssertion{

    private final String cardIdObj1;
    private final String cardIdObj2;

    private String actualCardId1;
    private String actualCardId2;

    public GlobalObjectivesGameAssertion(String card1, String card2){
        this.cardIdObj1 = card1;
        this.cardIdObj2 = card2;
    }

    @Override
    public boolean verifyAssertion(Game game) {
        actualCardId1 = game.getGlobalObjectives().getFirst().getId();
        actualCardId2 = game.getGlobalObjectives().getLast().getId();

        if(actualCardId1.isEmpty() || actualCardId2.isEmpty()){
            return false;
        }

        return Objects.equals(actualCardId1, cardIdObj1) && Objects.equals(actualCardId2, cardIdObj2);
    }

    @Override
    public String toString() {
        return String.format("GlobalObjectivesGameAssertion Assertion --- expectedCardOne: %s, expectedCardTwo: %s, actualCardOne: %s, actualCardTwo: %s", cardIdObj1, cardIdObj2, actualCardId1, actualCardId2);
    }
}
