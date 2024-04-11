package it.polimi.ingsw.gc28.games.assertions;

import it.polimi.ingsw.gc28.model.Game;


public class RoundsLeftAssertion extends GameAssertion{
    private final int roundsLeft;

    private Integer actualRoundsLeft;

    public RoundsLeftAssertion(int roundsLeft) {
        this.roundsLeft = roundsLeft;
    }

    @Override
    public boolean verifyAssertion(Game game) {
        if(game.getRoundsLeft().isPresent()){
            actualRoundsLeft = game.getRoundsLeft().get();
        }else{
            actualRoundsLeft = null;
        }

        return actualRoundsLeft != null && actualRoundsLeft == roundsLeft;
    }

    @Override
    public String toString() {
        return String.format("RoundsLeftAssertion --- expected: %d, actual: %d", roundsLeft, actualRoundsLeft);
    }
}
