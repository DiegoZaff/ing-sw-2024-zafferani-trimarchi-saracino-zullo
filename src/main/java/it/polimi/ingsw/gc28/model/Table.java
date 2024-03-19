package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.resources.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Table {


    private Map<Coordinate, Cell> mapPositions;

    // ? I would remove playableCoords and replace it with a function
    // ? isPlayable(Coordinate coord) which checks if coord is playable based on
    // ? unplayable coords and mapPositions (so we don't need to update playable coords each time)

    //      ^
    //      |
    // Mi sembra scomodo, inoltre sapendo map positions e unplayable coords non sai immediatamente
    // quelle giocabili, dovresti ad ogni iterazione controllare tutte le carte esterne con i vertici e
    // vedere se possono accettare una carta. Poi è anche utile nella comunicazione avere un elenco di
    // posizioni giocabili al momento, es da CLI puo dire posizioni giocabili: (0,1) ; (2,1), (1,-3) senza passare
    // per funzioni ulteriori



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
    public boolean checkPlayability(Coordinate coordinate){
        if (mapPositions.containsKey(coordinate)){
            return false;
        }
        // ? I would replace this two checks with isPlayable(Coordinate coord).
        // ? see comment above.
        if (unplayableCoords.contains(coordinate)){
            return false;
        }
        if (!playableCoords.contains(coordinate)){
            return false;
        }   // a questo punto non basta fare if (!playableCoords.contains(coordinate)){return true} e toglier ela riga sotto
        return true;
    }

    public void addMapPosition(Coordinate playCoordinate, Cell cell){
        mapPositions.put(playCoordinate, cell);
    }

    /**
     *
     * @param coordinate the coordinate of the cell
     * @return the cell, or null if the coordinate are not mapped
     */
    public Cell getCell(Coordinate coordinate){
        return mapPositions.get(coordinate);
    }

    /**
     * this method add the resources of a played card
     * @param coordinate the coordinate of the played card
     */
    private void sumResources(Coordinate coordinate){
        Map<Resource, Integer> mapResource = new HashMap<>();
        int temp;

        mapResource = getCell(coordinate).getResources();

        for (Resource key : mapResource.keySet()){
            temp = resourceCounters.get(key) + mapResource.get(key);

            resourceCounters.replace(key, temp);
        }
    }


    /**
     * this method removes from the counter the covered vertex
     * @param coordinate the cordinate of the played card
     */
    private void subResources (Coordinate coordinate){
        //da implementare
    }

    public void updateCounters (Coordinate coordinate){

        sumResources(coordinate);

        subResources(coordinate);

    }

    public void updateCoordinate (Coordinate coordinate){
        //da implementare
    }



}
