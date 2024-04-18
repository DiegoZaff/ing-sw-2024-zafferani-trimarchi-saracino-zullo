package it.polimi.ingsw.gc28.model.errors.types;

public class NameAlreadyChosenError extends PlayerActionError{
    public NameAlreadyChosenError() {
        super("Name already chosen! Choose another one!");
    }
}
