package it.polimi.ingsw.gc28.games.assertions;

import it.polimi.ingsw.gc28.model.Game;

public class PlayerRegisteredGameAssertion extends GameAssertion{

    private final int playersRegistered;
    private int actualPlayers;

    public PlayerRegisteredGameAssertion(int numberOfPlayers){
        this.playersRegistered = numberOfPlayers;
    }

    @Override
    public boolean verifyAssertion(Game game) {

        actualPlayers = game.getPlayers().size();

        return actualPlayers == playersRegistered;
    }

    @Override
    public String toString() {
        return String.format("PlayerRegisteredGameAssertion Assertion --- expectedPlayers: %d, actual: %d", playersRegistered, actualPlayers);
    }
}
