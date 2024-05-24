package it.polimi.ingsw.gc28.model.errors.types;

import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.model.errors.PlayerActionError;

public class UnrestorableGameError extends PlayerActionError {
    public UnrestorableGameError(ActionType action) {
        super("This game should not be restored! last action saved was: " + action);
    }
}
