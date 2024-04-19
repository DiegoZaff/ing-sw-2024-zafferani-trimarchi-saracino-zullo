package it.polimi.ingsw.gc28.model.errors.types;

import it.polimi.ingsw.gc28.model.errors.PlayerActionError;

public class NotYourTurnError extends PlayerActionError {
    public NotYourTurnError() {
        super("It's not your turn");
    }
}
