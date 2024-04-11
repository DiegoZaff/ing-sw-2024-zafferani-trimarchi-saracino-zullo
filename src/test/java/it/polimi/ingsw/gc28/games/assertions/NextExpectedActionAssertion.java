package it.polimi.ingsw.gc28.games.assertions;

import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.model.actions.utils.ActionType;

public class NextExpectedActionAssertion extends GameAssertion{

    private ActionType actionActual;
    private final ActionType actionExpected;

    public NextExpectedActionAssertion(ActionType actionExpected) {
        this.actionActual = null;
        this.actionExpected = actionExpected;
    }

    @Override
    public boolean verifyAssertion(Game game) {
        actionActual = game.actionExpected();

        return actionActual.equals((actionExpected));
    }

    @Override
    public String toString() {
        return String.format("NextExpectedActionAssertion --- expected: %s, actual: %s", actionExpected, actionActual);
    }
}
