package it.polimi.ingsw.gc28.model.objectives;

import it.polimi.ingsw.gc28.model.Cell;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.resources.Resource;

import java.util.Map;
import java.util.Optional;

public class ObjectiveSameResources extends Objective{

    public final Resource resource;

    public final int count;

    public ObjectiveSameResources(Resource resource, int count) {
        this.resource = resource;
        this.count = count;
    }

    @Override
    public int calculatePoints(Optional<Map<Coordinate, Cell>> mapPositions,
                               Optional<Map<Resource, Integer>> resourceCounters) throws IllegalArgumentException {
        return 0;
    }
}
