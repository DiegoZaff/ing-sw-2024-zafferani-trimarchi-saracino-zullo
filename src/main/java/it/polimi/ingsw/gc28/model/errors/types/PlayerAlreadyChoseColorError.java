package it.polimi.ingsw.gc28.model.errors.types;

import it.polimi.ingsw.gc28.model.errors.PlayerActionError;

public class PlayerAlreadyChoseColorError extends PlayerActionError {
    public PlayerAlreadyChoseColorError(String player) {
        super(String.format("%s already chose his color", player));
    }
}
