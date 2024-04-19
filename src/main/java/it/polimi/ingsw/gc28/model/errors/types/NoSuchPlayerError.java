package it.polimi.ingsw.gc28.model.errors.types;

import it.polimi.ingsw.gc28.model.errors.PlayerActionError;

public class NoSuchPlayerError extends PlayerActionError {
    public NoSuchPlayerError() {
        super("Non existent player!");
    }
}
