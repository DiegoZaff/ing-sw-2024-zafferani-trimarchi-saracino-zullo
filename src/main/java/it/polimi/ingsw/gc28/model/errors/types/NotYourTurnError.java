package it.polimi.ingsw.gc28.model.errors.types;

import it.polimi.ingsw.gc28.model.errors.PlayerActionError;

/**
 * this error gets triggered when a player tries to do an action outside his turn
 */
public class NotYourTurnError extends PlayerActionError {
    public NotYourTurnError() {
        super("It's not your turn");
    }
}
