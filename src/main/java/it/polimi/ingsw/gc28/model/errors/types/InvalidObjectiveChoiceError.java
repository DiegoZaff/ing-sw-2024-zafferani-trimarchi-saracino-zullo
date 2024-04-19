package it.polimi.ingsw.gc28.model.errors.types;

import it.polimi.ingsw.gc28.model.errors.PlayerActionError;

public class InvalidObjectiveChoiceError extends PlayerActionError {
    public InvalidObjectiveChoiceError(){
        super("Invalid Objective Card");
    }
}
