package it.polimi.ingsw.gc28.model.errors.types;

public class LobbyFullError extends PlayerActionError{
    public LobbyFullError() {
        super("Game lobby is full");
    }
}
