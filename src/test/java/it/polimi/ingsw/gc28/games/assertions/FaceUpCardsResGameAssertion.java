package it.polimi.ingsw.gc28.games.assertions;

import it.polimi.ingsw.gc28.model.Game;
import java.util.Objects;

public class FaceUpCardsResGameAssertion extends GameAssertion{
    private final String cardIdRes1;
    private final String cardIdRes2;

    private String actualCardId1;
    private String actualCardId2;

    public FaceUpCardsResGameAssertion(String card1, String card2){
        this.cardIdRes1 = card1;
        this.cardIdRes2 = card2;
    }

    @Override
    public boolean verifyAssertion(Game game) {
        actualCardId1 = game.getFaceUpResourceCards().getFirst().getId();
        actualCardId2 = game.getFaceUpResourceCards().getLast().getId();

        if(actualCardId1.isEmpty() || actualCardId2.isEmpty()){
            return false;
        }

        return Objects.equals(actualCardId1, cardIdRes1) && Objects.equals(actualCardId2, cardIdRes2);
    }

    @Override
    public String toString() {
        return String.format("FaceUpCardsResGameAssertion Assertion --- expectedCardOne: %s, expectedCardTwo: %s, actualCardOne: %s, actualCardTwo: %s", cardIdRes1, cardIdRes2, actualCardId1, actualCardId2);

    }
}
