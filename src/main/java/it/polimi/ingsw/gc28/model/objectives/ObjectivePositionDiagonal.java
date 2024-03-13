package it.polimi.ingsw.gc28.model.objectives;

import it.polimi.ingsw.gc28.model.Cell;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.resources.Resource;
import it.polimi.ingsw.gc28.model.resources.ResourcePrimary;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public class ObjectivePositionDiagonal extends Objective implements NeighboringCoordinateHelper {
    enum DiagonalType {
        MAIN_DIAGONAL,
        SECONDARY_DIAGONAL
    }
    public final DiagonalType type;

    public final ResourcePrimary resource;

    public ObjectivePositionDiagonal(DiagonalType type, ResourcePrimary resource, int points) {
        this.points = points;
        this.type = type;
        this.resource = resource;
    }

    @Override
    public int calculatePoints(Optional<Map<Coordinate, Cell>> mapPositions,
                               Optional<Map<Resource, Integer>> resourceCounters) throws  IllegalArgumentException{
        if(mapPositions.isEmpty()){
            throw new IllegalArgumentException();
        }

        Map<Coordinate, Cell> map = mapPositions.get();

        ArrayList<Coordinate[]> combinationList = new ArrayList<>();

        for(Coordinate coordinate : map.keySet()){

            Optional<ResourcePrimary> res = map.get(coordinate).card.getObjectiveResource();

            // resource type not present (initial card) or is present but is different than the objective's resource type.
            if(res.isEmpty() || resource.type != res.get().type){
                continue;
            }

            Coordinate[] neighborsCoordinates = getNeighborsCoordinates(coordinate);
            Coordinate one = neighborsCoordinates[0];
            Coordinate two = neighborsCoordinates[1];

            // coordinates not present or present but resource type is different => skip
            if(!map.containsKey(one) || !map.containsKey(two) ||
                    (map.get(one).card.getObjectiveResource().isPresent() &&
                            map.get(one).card.getObjectiveResource().get().type != resource.type ) ||
                    (map.get(two).card.getObjectiveResource().isPresent() &&
                            map.get(two).card.getObjectiveResource().get().type != resource.type )){
                continue;
            }

            // add combination which matches the objective to array of combinations.
            Coordinate[] newCombination = new Coordinate[3];

            newCombination[0] = one;
            newCombination[1] = two;
            newCombination[2] = coordinate;

            combinationList.add(newCombination);

        }

        ArrayList<Coordinate[]> bestCombinations = new ArrayList<>();
        for (int i = 0; i < combinationList.size(); i++){
            ArrayList<Coordinate[]> tempCombinations = new ArrayList<>();
            tempCombinations.add(combinationList.get(i));
            for(int j = 0; j < combinationList.size(); j++){
                if(i == j){
                    continue;
                }
                if(!areCombinationsOverlapping(combinationList.get(i), combinationList.get(j))){
                    tempCombinations.add(combinationList.get(j));
                }
            }

            if(tempCombinations.size() > bestCombinations.size()){
                // change pointer, tempCombinations will get reassigned.
                bestCombinations = tempCombinations;
            }
        }

        return bestCombinations.size() * points;
    }

    @Override
    public Coordinate[] getNeighborsCoordinates(Coordinate coord){
        Coordinate[] coords = new Coordinate[2];
        if(type == DiagonalType.MAIN_DIAGONAL){
            coords[0] = new Coordinate(coord.x + 1, coord.y + 1);
            coords[1] = new Coordinate(coord.x - 1, coord.y - 1);
        }else{
            coords[0] = new Coordinate(coord.x + 1, coord.y -1);
            coords[1] = new Coordinate(coord.x - 1, coord.y + 1);
        }
        return coords;
    }
}
