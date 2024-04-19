package it.polimi.ingsw.gc28.model.errors.types;

import it.polimi.ingsw.gc28.model.errors.PlayerActionError;

public class UnexpectedMoveError extends PlayerActionError {
    public UnexpectedMoveError() {
        super("It's not your turn");
    }
}
