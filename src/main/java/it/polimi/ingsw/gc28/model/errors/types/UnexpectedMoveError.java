package it.polimi.ingsw.gc28.model.errors.types;

import it.polimi.ingsw.gc28.model.errors.PlayerActionError;

/**
 * this error gets triggered when a player tries to play an illegal move
 */
public class UnexpectedMoveError extends PlayerActionError {
    public UnexpectedMoveError() {
        super("You can't play this move!");
    }
}
