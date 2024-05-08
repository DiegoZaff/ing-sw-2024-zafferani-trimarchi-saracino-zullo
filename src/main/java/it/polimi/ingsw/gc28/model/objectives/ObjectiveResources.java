package it.polimi.ingsw.gc28.model.objectives;
import it.polimi.ingsw.gc28.model.Cell;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.resources.Resource;

import java.io.Serializable;
import java.util.Map;

public class ObjectiveResources extends Objective implements Serializable {

    /**
     * This generalizes all combinations of resources.
     * if there's only one resource, then it will be a map with a single entry.
     */
    public final Map<Resource, Integer> resourcesNeeded;

    public ObjectiveResources(Map<Resource, Integer> resourcesNeeded, int points) {
        this.resourcesNeeded = resourcesNeeded;
        this.points = points;
    }

    @Override
    public int calculatePoints(Map<Coordinate, Cell> mapPositions,
                               Map<Resource, Integer> resourceCounters) throws IllegalArgumentException {

        int min = Integer.MAX_VALUE;
        for(Resource res : resourcesNeeded.keySet()){
            if(!resourceCounters.containsKey(res)){
                return 0;
            }

            int numberOfResources = resourceCounters.get(res);

            int nGroups = numberOfResources / resourcesNeeded.get(res);

            if(nGroups == 0){
                return 0;
            }

            if(nGroups < min){
                min = nGroups;
            }
        }

        if(min == Integer.MAX_VALUE){
            // no resources needed, something went wrong.
            return 0;
        }

        return min * points;
    }
}
