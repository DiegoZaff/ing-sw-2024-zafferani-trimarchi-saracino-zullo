package it.polimi.ingsw.gc28.model.errors.types;

import it.polimi.ingsw.gc28.model.errors.PlayerActionError;

public class PlayerIsAlreadyConnectedError extends PlayerActionError {
    public PlayerIsAlreadyConnectedError(String playerName) {
        super(String.format("%s, you are already connected, are you??", playerName));
    }
}
