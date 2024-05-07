package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.cards.CardResource;
import it.polimi.ingsw.gc28.model.resources.Resource;
import it.polimi.ingsw.gc28.model.resources.ResourcePrimary;
import it.polimi.ingsw.gc28.model.resources.ResourceSpecial;
import it.polimi.ingsw.gc28.model.resources.utils.ResourcePrimaryType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceSpecialType;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Table implements Serializable {


    private final Map<Coordinate, Cell> mapPositions;

    private final ArrayList<Coordinate> playableCoords;

    private final ArrayList<Coordinate> unplayableCoords;

    private final Map<Resource, Integer> resourceCounters;


    public Map<Coordinate, Cell> getMapPositions(){
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

        playableCoords.add(new Coordinate(0,0));

        for (ResourcePrimaryType r : ResourcePrimaryType.values()){
            resourceCounters.put(new ResourcePrimary(r), 0);
        }
        for (ResourceSpecialType r : ResourceSpecialType.values()){
            resourceCounters.put(new ResourceSpecial(r), 0);
        }
        //prova
    }


    public void removePlayableCoordinate (Coordinate coordinatesToRemove){
        int n = 0;
        if (playableCoords.contains(coordinatesToRemove)){
            for (Coordinate c : playableCoords){
                if (c.equals(coordinatesToRemove)){
                     playableCoords.remove(n);
                     break;
                }
                n++;
            }
        }
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
        // a questo punto non basta fare if (!playableCoords.contains(coordinate)){return true} e toglier ela riga sotto
        return playableCoords.contains(coordinate);
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
            playableCoords.add(c);
        }
    }

    private void hasNotCornerUpdate(Coordinate c){
        unplayableCoords.add(c);
        removePlayableCoordinate(c);
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


    @Override
    public String toString(){
//        ArrayList<Coordinate> coords = new ArrayList<>(mapPositions.keySet());
//
//        ArrayList<Coordinate> orderedCoords = coords.stream().sorted(Comparator.comparingInt(Coordinate::getY))
//                .sorted(Comparator.comparingInt(Coordinate::getX)).collect(Collectors.toCollection(ArrayList::new));



        String cardIdsString = mapPositions.values().stream().map(cell -> cell.getCard().getId()).collect(Collectors.joining(", "));

        return String.format("Cards on the table are: %s", cardIdsString);

    }
}
