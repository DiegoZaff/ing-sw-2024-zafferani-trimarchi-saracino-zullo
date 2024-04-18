package it.polimi.ingsw.gc28.model.errors.types;

public class NoSuchPlayerError extends PlayerActionError{
    public NoSuchPlayerError() {
        super("Non existent player!");
    }
}
