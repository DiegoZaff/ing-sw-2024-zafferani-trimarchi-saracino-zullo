package it.polimi.ingsw.gc28.model.errors.types;

import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.errors.PlayerActionError;

public class UnplayableCoordinate  extends PlayerActionError {

    public UnplayableCoordinate(Coordinate coord) {
        super("Unplayable coord: " + coord);

    }
}
