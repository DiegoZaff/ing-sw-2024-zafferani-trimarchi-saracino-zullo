package it.polimi.ingsw.gc28.model.errors.types;

import it.polimi.ingsw.gc28.model.errors.PlayerActionError;

public class NameAlreadyChosenError extends PlayerActionError {
    public NameAlreadyChosenError() {
        super("Name already chosen! Choose another one!");
    }
}
