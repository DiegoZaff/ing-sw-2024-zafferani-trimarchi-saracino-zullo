package it.polimi.ingsw.gc28.model.errors;

public class UnexpectedMoveError extends  PlayerActionError{
    public UnexpectedMoveError() {
        super("It's not your turn");
    }
}
