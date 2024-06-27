package it.polimi.ingsw.gc28.model.objectives;
import it.polimi.ingsw.gc28.model.Cell;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.resources.Resource;
import it.polimi.ingsw.gc28.view.utils.Colors;

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

    /**
     * this method awards the correc tnumber of points for the resource objective
     * @param mapPositions of the tabòe
     * @param resourceCounters of the table
     * @throws IllegalArgumentException
     */
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

    private String resourceNeededToString (){
        int i;
        StringBuffer s = new StringBuffer();
        for (Resource r : resourcesNeeded.keySet()){
            for (i = 0; i< resourcesNeeded.get(r); i++){
                s.append(r.toString());
            }
        }
        return s.toString();
    }

    private int resourceNeededCounter(){
        int i = 0;
        for (Resource r : resourcesNeeded.keySet()){
            i += resourcesNeeded.get(r);
        }

        return i;
    }


    @Override
    public String toString(){
        String card = String.format("""
                __________________
                |       %s        |
                |       %s       |
                |                |
                ------------------
                """, points, resourceNeededToString());
        StringBuffer show = new StringBuffer(card);
        int offset = resourceNeededCounter();
        show.delete(39,39+offset-1);
        show.delete(45+11*offset,45+11*offset+(offset-1));

        return show.toString();
    }
}
