package it.polimi.ingsw.gc28.model.objectives;

import it.polimi.ingsw.gc28.model.Cell;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.resources.Resource;

import java.util.Map;
import java.util.Optional;

public abstract class Objective {

    public int points;

    public abstract int calculatePoints(Optional<Map<Coordinate, Cell>> mapPositions,
                                        Optional<Map<Resource,Integer>> resourceCounters) throws  IllegalArgumentException;
}
