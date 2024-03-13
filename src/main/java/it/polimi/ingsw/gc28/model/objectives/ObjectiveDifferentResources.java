package it.polimi.ingsw.gc28.model.objectives;

import it.polimi.ingsw.gc28.model.Cell;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.resources.Resource;
import it.polimi.ingsw.gc28.model.resources.ResourceSpecial;

import java.util.Map;
import java.util.Optional;

public class ObjectiveDifferentResources extends Objective{
    public final Map<ResourceSpecial, Integer> resources;

    public ObjectiveDifferentResources(Map<ResourceSpecial, Integer> resources) {
        this.resources = resources;
    }

    @Override
    public int calculatePoints(Optional<Map<Coordinate, Cell>> mapPositions,
                               Optional<Map<Resource, Integer>> resourceCounters) throws IllegalArgumentException {
        return 0;
    }
}
