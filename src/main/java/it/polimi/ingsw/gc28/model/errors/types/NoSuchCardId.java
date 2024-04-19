package it.polimi.ingsw.gc28.model.errors.types;

import it.polimi.ingsw.gc28.model.errors.PlayerActionError;

public class NoSuchCardId extends PlayerActionError {
    public NoSuchCardId(String id) {
        super("This id doesnt exists: " + id);
    }
}
