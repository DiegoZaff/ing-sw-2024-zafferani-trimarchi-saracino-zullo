package it.polimi.ingsw.gc28.model.errors.types;

import it.polimi.ingsw.gc28.model.errors.PlayerActionError;

public class AlreadyChoseObjectiveError  extends PlayerActionError {
    public AlreadyChoseObjectiveError() {
        super("You already chose the objective");
    }
}
