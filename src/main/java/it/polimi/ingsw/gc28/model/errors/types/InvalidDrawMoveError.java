package it.polimi.ingsw.gc28.model.errors.types;

import it.polimi.ingsw.gc28.model.errors.PlayerActionError;

public class InvalidDrawMoveError extends PlayerActionError {
    public InvalidDrawMoveError() {
        super("You can't draw this card");
    }
}
