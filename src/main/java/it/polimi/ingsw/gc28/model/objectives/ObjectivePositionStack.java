package it.polimi.ingsw.gc28.model.objectives;

import it.polimi.ingsw.gc28.model.Cell;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.resources.Resource;
import it.polimi.ingsw.gc28.model.resources.ResourcePrimary;

import java.util.Map;
import java.util.Optional;

public class ObjectivePositionStack extends Objective{
    @Override
    public int calculatePoints(Optional<Map<Coordinate, Cell>> mapPositions,
                               Optional<Map<Resource, Integer>> resourceCounters) throws IllegalArgumentException {
        return 0;
    }

    enum PositionStackType{
        N_E_STACK,
        N_W_STACK,
        S_E_STACK,
        S_W_STACK
    }
    public final PositionStackType type;

    public final ResourcePrimary centralResource;

    public final ResourcePrimary cornerResource;

    public ObjectivePositionStack(PositionStackType type, ResourcePrimary centralResource, ResourcePrimary cornerResource) {
        this.type = type;
        this.centralResource = centralResource;
        this.cornerResource = cornerResource;
    }
}
