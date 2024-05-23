package it.polimi.ingsw.gc28.model.errors.types;

import it.polimi.ingsw.gc28.model.errors.PlayerActionError;

public class ColorTakenError extends PlayerActionError {
    public ColorTakenError() {
        super("Color not available");
    }
}
