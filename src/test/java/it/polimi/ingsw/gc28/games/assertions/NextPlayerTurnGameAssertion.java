package it.polimi.ingsw.gc28.games.assertions;

import it.polimi.ingsw.gc28.model.Game;

public class NextPlayerTurnGameAssertion extends GameAssertion {
    
    private final String nextPlayerNick;

    private String actualPlayerNick;

    public NextPlayerTurnGameAssertion(String nextPlayerNick) {
        this.nextPlayerNick = nextPlayerNick;
    }

    @Override
    public boolean verifyAssertion(Game game) {

        actualPlayerNick = game.playerToPlay().getName();
        
        return actualPlayerNick.equals(nextPlayerNick);
    }

    @Override
    public String toString() {
        return String.format("NextPlayerOfTurn Assertion: expected: %s, actual: %s", nextPlayerNick, actualPlayerNick);
    }
}
