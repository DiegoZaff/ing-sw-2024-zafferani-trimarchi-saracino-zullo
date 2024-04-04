package it.polimi.ingsw.gc28.model.errors.types;

public class NotYourTurnError extends PlayerActionError{
    public NotYourTurnError() {
        super("It's not your turn");
    }
}
