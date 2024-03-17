package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.resources.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Table {


    private Map<Coordinate, Cell> mapPositions;

    public ArrayList<Coordinate> getPlayableCoords() {
        return playableCoords;
    }

    private ArrayList<Coordinate> playableCoords;

    private ArrayList<Coordinate> unplayableCoords;

    private Map<Resource, Integer> resourceCounters;


    public ArrayList<Coordinate> getUnplayableCoords() {
        return unplayableCoords;
    }


    public Map<Coordinate, Cell> GetMapPositions(){
        return mapPositions;
    }

    public Map<Resource, Integer> getResourceCounters() {
        return resourceCounters;
    }

    public Table(){
        mapPositions = new HashMap<>();
        resourceCounters = new HashMap<>();
        playableCoords = new ArrayList<>();
        unplayableCoords = new ArrayList<>();

        //! recourceCounter must be initialized
    }


    public void addUnplayableCoordinate (Coordinate coordinatesToAdd){

        unplayableCoords.add(coordinatesToAdd);
    }

    public void addPlayableCoordinate (Coordinate coordinatesToAdd){

        playableCoords.add(coordinatesToAdd);
    }

    public void removePlayableCoordinate (Coordinate coordinatesToRemove){


        playableCoords.remove(coordinatesToRemove);
    }


    public boolean alreadyUnplayable (Coordinate coordinateToCheck){

        for ( Coordinate coordinate : unplayableCoords)
        {
            if (coordinateToCheck == coordinate)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * this method checks if the given coordinate can be played
     * @param coordinate the coordinate to be checked
     * @return true if the coordinate can be played,
     * false if not
     */
    public boolean CheckPlayabilty(Coordinate coordinate){
        if (mapPositions.keySet().contains(coordinate)){
            return false;
        }
        if (unplayableCoords.contains(coordinate)){
            return false;
        }
        if (!playableCoords.contains(coordinate)){
            return false;
        }
        return true;
    }

    public  void AddMapPosition(Coordinate playCoordinate, Cell cell){
        mapPositions.put(playCoordinate, cell);
    }


}
