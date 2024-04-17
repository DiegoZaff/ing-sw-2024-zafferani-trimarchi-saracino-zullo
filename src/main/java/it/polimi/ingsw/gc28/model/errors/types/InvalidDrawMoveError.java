package it.polimi.ingsw.gc28.model.errors.types;

public class InvalidDrawMoveError extends  PlayerActionError{
    public InvalidDrawMoveError() {
        super("You can't draw this card");
    }
}
