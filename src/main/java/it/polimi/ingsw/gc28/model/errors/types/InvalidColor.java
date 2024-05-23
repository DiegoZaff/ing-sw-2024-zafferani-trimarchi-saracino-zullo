package it.polimi.ingsw.gc28.model.errors.types;

import it.polimi.ingsw.gc28.model.errors.PlayerActionError;

public class InvalidColor extends PlayerActionError {
    public InvalidColor(String color) {
        super(color + " doesn't exist");
    }
}
