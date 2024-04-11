package it.polimi.ingsw.gc28.games.assertions;

import it.polimi.ingsw.gc28.model.Game;

public class GameStartedGameAssertion extends GameAssertion{

    public boolean gameStatus;
    public boolean actualGameStatus;
    public GameStartedGameAssertion(boolean state){
        this.gameStatus = state;
    }

    @Override
    public boolean verifyAssertion(Game game) {
        actualGameStatus = game.getHasGameStarted();

        return actualGameStatus == gameStatus;
    }

    @Override
    public String toString() {
        return String.format("GameStartedGameAssertion Assertion --- expectedState: %s, actual: %s", gameStatus, actualGameStatus);
    }
}
