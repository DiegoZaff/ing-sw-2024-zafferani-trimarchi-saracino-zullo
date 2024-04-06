package it.polimi.ingsw.gc28.games.assertions;

import it.polimi.ingsw.gc28.model.Game;

public abstract class GameAssertion {

    public abstract boolean verifyAssertion(Game game);

    public abstract String toString();
}
