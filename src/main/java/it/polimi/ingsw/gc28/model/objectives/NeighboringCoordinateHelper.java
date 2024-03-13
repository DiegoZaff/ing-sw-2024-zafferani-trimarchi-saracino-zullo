package it.polimi.ingsw.gc28.model.objectives;

import it.polimi.ingsw.gc28.model.Coordinate;

public interface NeighboringCoordinateHelper {

    public abstract Coordinate[] getNeighborsCoordinates(Coordinate coord);


    default boolean areCombinationsOverlapping(Coordinate[] comb1, Coordinate[] comb2){
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
