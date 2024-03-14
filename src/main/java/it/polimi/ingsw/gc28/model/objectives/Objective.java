package it.polimi.ingsw.gc28.model.objectives;
import it.polimi.ingsw.gc28.model.Cell;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.resources.Resource;
import java.util.ArrayList;
import java.util.Map;

public abstract class Objective {

    public int points;

    public abstract int calculatePoints(Map<Coordinate, Cell> mapPositions,
                                        Map<Resource,Integer> resourceCounters) throws  IllegalArgumentException;

    protected boolean areCombinationsOverlapping(ArrayList<Coordinate> comb1, ArrayList<Coordinate> comb2){
        for(Coordinate coord1 : comb1){
            for(Coordinate coord2 : comb2){
                if(coord1.x == coord2.x && coord1.y == coord2.y){
                    return true;
                }
            }
        }
        return false;
    }
}
