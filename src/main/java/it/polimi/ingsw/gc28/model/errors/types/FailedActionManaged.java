package it.polimi.ingsw.gc28.model.errors.types;

import it.polimi.ingsw.gc28.model.errors.PlayerActionError;

/**
 * This error is used to notify the GamesManager and controller that an action failed,
 * nonetheless it was already managed by the game controller.
 *
 * For example, thanks to this, the GamesManager knows that the game creation, joining, reconnection
 * didn't go through, so it shouldn't attach the stub of the game to the client.
 */
public class FailedActionManaged extends PlayerActionError {
    public FailedActionManaged(String error) {
        super(error);
    }
}
