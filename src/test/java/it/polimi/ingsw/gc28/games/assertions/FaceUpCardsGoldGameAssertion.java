package it.polimi.ingsw.gc28.games.assertions;

import it.polimi.ingsw.gc28.model.Game;

import java.util.Objects;

public class FaceUpCardsGoldGameAssertion extends GameAssertion{

    private final String cardIdGold1;
    private final String cardIdGold2;

    private String actualCardId1;
    private String actualCardId2;

    public FaceUpCardsGoldGameAssertion(String card1, String card2){
        this.cardIdGold1 = card1;
        this.cardIdGold2 = card2;
    }
    @Override
    public boolean verifyAssertion(Game game) {
        actualCardId1 = game.getFaceUpGoldCards().getFirst().getId();
        actualCardId2 = game.getFaceUpGoldCards().getLast().getId();

        if(actualCardId1.isEmpty() || actualCardId2.isEmpty()){
            return false;
        }

        return Objects.equals(actualCardId1, cardIdGold1) && Objects.equals(actualCardId2, cardIdGold2);
    }

    @Override
    public String toString() {
        return String.format("FaceUpCardsResGameAssertion Assertion --- expectedCardOne: %s, expectedCardTwo: %s, actualCardOne: %s, actualCardTwo: %s", cardIdGold1, cardIdGold2, actualCardId1, actualCardId2);
    }
}
