package it.polimi.ingsw.gc28.games.assertions;

import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.model.Player;

import java.util.Objects;
import java.util.Optional;

public class FirstPlayerGameAssertion extends GameAssertion{

    private final String firstPlayer;
    private String actualFirstPlayer;

    public FirstPlayerGameAssertion(String nick){
        this.firstPlayer = nick;
    }

    @Override
    public boolean verifyAssertion(Game game) {
        actualFirstPlayer = game.getFirstPlayer().getName();

        if(actualFirstPlayer.isEmpty()){
            return false;
        }

        return Objects.equals(firstPlayer, actualFirstPlayer);
    }

    @Override
    public String toString() {
        return String.format("FirstPlayerGameAssertion Assertion --- expected: %s, actual: %s", firstPlayer, actualFirstPlayer);
    }
}
