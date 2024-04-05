package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.resources.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    private Optional<Cell> getNWCell (Coordinate coordinate) {
        Coordinate x = new Coordinate(coordinate.getX() - 1, coordinate.getY() + 1);
        return Optional.ofNullable(getCell(x));
    }

    public boolean CellNWPresent(Coordinate coordinate){
        return this.getNWCell(coordinate).isPresent();
    }

    private Optional<Resource> getNWCoveredResource(Coordinate coordinate){
        if (this.getNWCell(coordinate).isPresent()){
            return this.getNWCell(coordinate).get().getSEResource();
        } else {
            return Optional.empty();
        }
    }

    private Optional<Cell> getNECell (Coordinate coordinate) {
        Coordinate x = new Coordinate(coordinate.getX() + 1, coordinate.getY() + 1);
        return Optional.ofNullable(getCell(x));
    }

    public boolean CellNEPresent(Coordinate coordinate){
        return this.getNECell(coordinate).isPresent();
    }


    private Optional<Resource> getNECoveredResource(Coordinate coordinate){
        if (this.getNECell(coordinate).isPresent()){
            return this.getNECell(coordinate).get().getSWResource();
        } else {
            return Optional.empty();
        }
    }

    private Optional<Cell> getSECell (Coordinate coordinate) {
        Coordinate x = new Coordinate(coordinate.getX() + 1, coordinate.getY() - 1);
        return Optional.ofNullable(getCell(x));
    }

    public boolean CellSEPresent(Coordinate coordinate){
        return this.getSECell(coordinate).isPresent();
    }

    private Optional<Resource> getSECoveredResource(Coordinate coordinate){
        if (this.getSECell(coordinate).isPresent()){
            return this.getSECell(coordinate).get().getNWResource();
        } else {
            return Optional.empty();
        }
    }

    private Optional<Cell> getSWCell (Coordinate coordinate) {
        Coordinate x = new Coordinate(coordinate.getX() - 1, coordinate.getY() - 1);
        return Optional.ofNullable(getCell(x));
    }

    public boolean CellSWPresent(Coordinate coordinate){
        return this.getSWCell(coordinate).isPresent();
    }

    private Optional<Resource> getSWCoveredResource(Coordinate coordinate){
        if (this.getSWCell(coordinate).isPresent()){
            return this.getSWCell(coordinate).get().getNEResource();
        } else {
            return Optional.empty();
        }
    }
    //potremmo compattare questi metodi, da rivedere


    /**
     * this method return the resource that are covered when a card is played
     * @param coordinate te coordinate where the card is played
     * @return an ArrayList of Optional resource, containing the covered resources
     */
    public ArrayList<Optional<Resource>>  getCoveredResource (Coordinate coordinate){
        ArrayList<Optional<Resource>> a = new ArrayList<>();
        a.add(getNWCoveredResource(coordinate));
        a.add(getNECoveredResource(coordinate));
        a.add(getSECoveredResource(coordinate));
        a.add(getSWCoveredResource(coordinate));

        return a;
    }

    /**
     * this method add the resources of a played card
     * @param coordinate the coordinate of the played card
     */
    private void sumResources(Coordinate coordinate){
        Map<Resource, Integer> mapResource;
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
        int temp;
        for (Optional<Resource> a : this.getCoveredResource(coordinate)){
            if (a.isPresent()){
                temp = resourceCounters.get(a.get()) - 1;
                resourceCounters.replace(a.get(), temp);
            }
        }
    }

    /**
     * this method updates resource counter, based on how and where the card has been played
     * @param coordinate the coordinate where the card has been played
     */
    public void updateCounters (Coordinate coordinate){

        sumResources(coordinate);

        subResources(coordinate);

    }
    private void hasCornerUpdate(Coordinate c){
        if (!mapPositions.containsKey(c) && !unplayableCoords.contains(c)){
            addPlayableCoordinate(c);
        }
    }

    private void hasNotCornerUpdate(Coordinate c){
        addUnplayableCoordinate(c);
        if (playableCoords.contains(c)){
            removePlayableCoordinate(c);
        }
    }

    private void updateNWCoordinate(Coordinate coordinate){
        Coordinate c = new Coordinate(coordinate.getX()-1, coordinate.getY()+1);
        if(getCell(coordinate).getNWExists()){
            //se ha angolo
            hasCornerUpdate(c);
        } else {
            //se non ha angolo
            hasNotCornerUpdate(c);
        }
    }

    private void updateNECoordinate(Coordinate coordinate){
        Coordinate c = new Coordinate(coordinate.getX()+1, coordinate.getY()+1);
        if(getCell(coordinate).getNEExists()){
            //se ha angolo
            hasCornerUpdate(c);
        } else {
            //se non ha angolo
            hasNotCornerUpdate(c);
        }
    }

    private void updateSECoordinate(Coordinate coordinate){
        Coordinate c = new Coordinate(coordinate.getX()+1, coordinate.getY()-1);
        if(getCell(coordinate).getSEExists()){
            //se ha angolo
            hasCornerUpdate(c);
        } else {
            //se non ha angolo
            hasNotCornerUpdate(c);
        }
    }

    private void updateSWCoordinate(Coordinate coordinate){
        Coordinate c = new Coordinate(coordinate.getX()-1, coordinate.getY()-1);
        if(getCell(coordinate).getSWExists()){
            //se ha angolo
            hasCornerUpdate(c);
        } else {
            //se non ha angolo
            hasNotCornerUpdate(c);
        }
    }

    /**
     * this method updates playable and unplayable coordinate in table, based on how and where the card is played
     * @param coordinate the coordinate where the card has been played
     */
    public void updateCoordinate (Coordinate coordinate){
        updateNWCoordinate(coordinate);
        updateNECoordinate(coordinate);
        updateSECoordinate(coordinate);
        updateSWCoordinate(coordinate);
    }



}
