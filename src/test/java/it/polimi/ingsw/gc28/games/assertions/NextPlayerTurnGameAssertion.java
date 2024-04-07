package it.polimi.ingsw.gc28.games.assertions;

import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.model.Player;

import java.util.Optional;

public class NextPlayerTurnGameAssertion extends GameAssertion {
    
    private final String nextPlayerNick;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<String> actualPlayerNick;

    public NextPlayerTurnGameAssertion(String nextPlayerNick) {
        this.nextPlayerNick = nextPlayerNick;
    }

    @Override
    public boolean verifyAssertion(Game game) {

        actualPlayerNick = game.playerToPlay().map(Player::getName);

        return actualPlayerNick.map(s -> s.equals(nextPlayerNick)).orElse(false);

    }

    @Override
    public String toString() {
        return String.format("NextPlayerOfTurn Assertion: expected: %s, actual: %s", nextPlayerNick, actualPlayerNick);
    }
}
