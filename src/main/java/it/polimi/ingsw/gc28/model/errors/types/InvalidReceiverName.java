package it.polimi.ingsw.gc28.model.errors.types;

import it.polimi.ingsw.gc28.model.errors.PlayerActionError;

public class InvalidReceiverName extends PlayerActionError {
    public InvalidReceiverName() {
        super("The player you want to send a message doesn't exist");
    }
}
