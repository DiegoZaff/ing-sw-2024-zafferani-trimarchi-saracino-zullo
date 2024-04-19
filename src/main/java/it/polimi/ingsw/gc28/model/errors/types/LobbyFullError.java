package it.polimi.ingsw.gc28.model.errors.types;

import it.polimi.ingsw.gc28.model.errors.PlayerActionError;

public class LobbyFullError extends PlayerActionError {
    public LobbyFullError() {
        super("Game lobby is full");
    }
}
