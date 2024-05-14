package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.view.utils.TuiStringHelper;
import it.polimi.ingsw.gc28.model.resources.Resource;
import it.polimi.ingsw.gc28.model.resources.ResourcePrimary;
import it.polimi.ingsw.gc28.model.resources.ResourceSpecial;
import it.polimi.ingsw.gc28.model.resources.utils.ResourcePrimaryType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceSpecialType;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        ArrayList<Coordinate> coords = new ArrayList<>(mapPositions.keySet());

        // TODO : useless, remember to remove this
        // decreasing y order, increasing x order
        ArrayList<Coordinate> orderedCoords = coords.stream()
                .sorted((o1, o2) -> {
                    if (o1.getY() == o2.getY()) {
                        // If y values are equal, compare by x in ascending order
                        return Integer.compare(o1.getX(), o2.getX());
                    } else {
                        // Otherwise, compare by y in descending order
                        return Integer.compare(o2.getY(), o1.getY());
                    }
                })
                .collect(Collectors.toCollection(ArrayList::new));


        /*

            <-----13----><-5-><------13---><-5-><-----13----><-5->

            _____________     _____________     <---- firstLayer
            |%s       %s|     |%s       %s|     <---- secondLayer
            |     %s    |     |     %s    |     <---- thirdLayer
            |%s      _____________        |     <---- fourthLayer
            ‾‾‾‾‾‾‾‾‾|%s       %s|‾‾‾‾‾‾‾‾‾     <---- fifthLayer
            <---9--->|     %s    |<---9--->
                     |%s       %s|
                     ‾‾‾‾‾‾‾‾‾‾‾‾‾
                            x=0 y=0
         */

        Map<Coordinate, String[]> mapLayers = new HashMap<>();

        for(Coordinate coord : orderedCoords){
            Cell cell = mapPositions.get(coord);
            int currPlayOrder = cell.getOrderPlay();

            ArrayList<String> verticesStrings= TuiStringHelper.getVerticesStringInfo(cell.getCard(), cell.getIsPlayedFront());
            String centralRes = cell.getCard().getCentralResourceStringInfo(cell.getIsPlayedFront());

            Optional<Cell> NECell = getNECell(coord);
            Optional<Cell> NWCell = getNWCell(coord);
            Optional<Cell> SECell = getSECell(coord);
            Optional<Cell> SWCell = getSWCell(coord);

            String firstLayer;
            String firstLayer1;
            String firstLayer2;
            String secondLayer;
            String secondLayer1;
            String secondLayer2;
            String thirdLayer;
            String fourthLayer;
            String fourthLayer1;
            String fourthLayer2;
            String fifthLayer;
            String fifthLayer1;
            String fifthLayer2;

            //1st and 2nd layer
            if(NWCell.isEmpty() || NWCell.get().getOrderPlay() < currPlayOrder){
                firstLayer1 = "______";
                secondLayer1 = String.format("|%s   ", verticesStrings.getFirst());
            }else{
                firstLayer1 = "__";
                secondLayer1 = "  ";
            }

            if(NECell.isEmpty() || NECell.get().getOrderPlay() < currPlayOrder){
                firstLayer2 = "______";
                secondLayer2 = String.format("   %s|", verticesStrings.get(1));
            }else{
                firstLayer2 = "__";
                secondLayer2 = "  ";
            }

            //4th and 5th layer
            if(SWCell.isEmpty() || SWCell.get().getOrderPlay() < currPlayOrder){
                fourthLayer1 = String.format("|%s   ", verticesStrings.get(3));
                fifthLayer1 = "‾‾‾‾‾‾";
            }else{
                fourthLayer1 = "  ";
                fifthLayer1 = "‾‾";
            }

            if(SECell.isEmpty() || SECell.get().getOrderPlay() < currPlayOrder){
                fourthLayer2 = String.format("   %s|", verticesStrings.get(2));
                fifthLayer2 = "‾‾‾‾‾‾";
            }else{
                fourthLayer2 = "  ";
                fifthLayer2 = "‾‾";
            }

            firstLayer = firstLayer1 + "_" + firstLayer2;
            secondLayer = secondLayer1 + " " + secondLayer2;

            //Match something like: \u1490[32mLu\u1404[0m
            String regex = "\\X\\[\\d{2}m([a-zA-Z\\s]+)\\X\\[0m"; //

            // Compile the pattern
            Pattern pattern = Pattern.compile(regex);

            // Create a matcher for the input string
            Matcher matcher = pattern.matcher(centralRes);

            int lengthResString = 0;
            while(matcher.find()){
                lengthResString += 2;
            }

            int leftPadding = (11 - lengthResString) / 2;
            int rightPadding = 11 - leftPadding - lengthResString;
            thirdLayer ="|" + " ".repeat(leftPadding) + centralRes + " ".repeat(rightPadding) + "|" ;
            fourthLayer = fourthLayer1 + " " + fourthLayer2;
            fifthLayer = fifthLayer1 + "‾" + fifthLayer2;

            String[] cardLayers = {firstLayer, secondLayer, thirdLayer, fourthLayer, fifthLayer};

            mapLayers.put(coord, cardLayers);
        }


        String emptySpace5 = "     ";
        String emptySpace13 = "             ";
        String emptySpace9 = "         ";
        String emptySpace4 = "    ";

        StringBuilder result = new StringBuilder();


        int minX = orderedCoords.stream().min(Comparator.comparingInt(Coordinate::getX)).orElseThrow().getX();
        int maxX = orderedCoords.stream().max(Comparator.comparingInt(Coordinate::getX)).orElseThrow().getX();
        int minY = orderedCoords.stream().min(Comparator.comparingInt(Coordinate::getY)).orElseThrow().getY();
        int maxY = orderedCoords.stream().max(Comparator.comparingInt(Coordinate::getY)).orElseThrow().getY();

        Coordinate curr;

        int startingK = 0;

        // these are just to keep track of coordinates whose cell's first and second layer were already printed.
        ArrayList<Coordinate> firstLayerAlreadyPrinted = new ArrayList<>();
        ArrayList<Coordinate> secondLayerAlreadyPrinted = new ArrayList<>();


        for (int j = maxY; j > minY - 1 ; j--){
            for (int k = startingK; k < 5; k++){
                for (int i = minX ; i < maxX + 1; i++){

                    curr = new Coordinate(i, j);

                    if((Math.abs(curr.getX()) + Math.abs(curr.getY()))% 2 != 0){

                        if(k == 0 || k == 1 || k == 2){
                            if(i == minX){
                                result.append(emptySpace9);
                            }else{
                                result.append(emptySpace5);
                            }
                        }else{
                            Coordinate downCoord = new Coordinate(curr.getX(), curr.getY() - 1);

                            String[] downCoordStrings = mapLayers.get(downCoord);

                            if(downCoordStrings == null){
                                if(i == minX){
                                    result.append(emptySpace9);
                                }else{
                                    result.append(emptySpace5);
                                }
                            }
                            //else do nothing
                        }

                    }else{
                        String[] currStrings = mapLayers.get(curr);

                        if(currStrings == null){
                            if(k == 0 || k == 1 || k == 2){
                                result.append(emptySpace13);
                            }else{
                                if(k == 3){
                                    Coordinate adjacentCoordLeft = new Coordinate(curr.getX() - 1, curr.getY() - 1);

                                    String[] stringsAdjLeft = mapLayers.get(adjacentCoordLeft);

                                    if(stringsAdjLeft != null){
                                        if(!firstLayerAlreadyPrinted.contains(adjacentCoordLeft)){
                                            result.append(stringsAdjLeft[0]);
                                            firstLayerAlreadyPrinted.add(adjacentCoordLeft);
                                        }
                                        result.append(emptySpace5);
                                    }else{
                                        result.append(emptySpace9);
                                    }

                                    Coordinate adjacentCoordRight = new Coordinate(curr.getX() + 1, curr.getY() - 1);

                                    String[] stringsAdjRight = mapLayers.get(adjacentCoordRight);

                                    if(stringsAdjRight != null){
                                        if(!firstLayerAlreadyPrinted.contains(adjacentCoordRight)){
                                            result.append(stringsAdjRight[0]);
                                            firstLayerAlreadyPrinted.add(adjacentCoordRight);
                                        }
                                    }else{
                                        result.append(emptySpace4);
                                    }
                                }else{
                                    Coordinate adjacentCoordLeft = new Coordinate(curr.getX() - 1, curr.getY() - 1);

                                    String[] stringsAdjLeft = mapLayers.get(adjacentCoordLeft);

                                    if(stringsAdjLeft != null){
                                        if(!secondLayerAlreadyPrinted.contains(adjacentCoordLeft)){
                                            result.append(stringsAdjLeft[1]);
                                            secondLayerAlreadyPrinted.add(adjacentCoordLeft);
                                        }
                                        result.append(emptySpace5);
                                    }else{
                                        result.append(emptySpace9);
                                    }

                                    Coordinate adjacentCoordRight = new Coordinate(curr.getX() + 1, curr.getY() - 1);

                                    String[] stringsAdjRight = mapLayers.get(adjacentCoordRight);

                                    if(stringsAdjRight != null){
                                        if(!secondLayerAlreadyPrinted.contains(adjacentCoordRight)){
                                            result.append(stringsAdjRight[1]);
                                            secondLayerAlreadyPrinted.add(adjacentCoordRight);
                                        }
                                    }else{
                                        result.append(emptySpace4);
                                    }
                                }
                            }

                        }else{
                            //first layer
                            if(k == 0){
                                result.append(currStrings[0]);
                            }else if(k == 1){
                                result.append(currStrings[1]);
                            }else if(k == 2){
                                result.append(currStrings[2]);
                            }else if(k == 3){
                                Coordinate adjacentCoordLeft = new Coordinate(curr.getX() - 1, curr.getY() - 1);

                                String[] stringsAdjLeft = mapLayers.get(adjacentCoordLeft);

                                if(stringsAdjLeft == null){
                                    result.append(currStrings[3]);
                                }else{
                                    if(!firstLayerAlreadyPrinted.contains(adjacentCoordLeft)){
                                        result.append(stringsAdjLeft[0]);
                                        firstLayerAlreadyPrinted.add(adjacentCoordLeft);
                                    }

                                    result.append(currStrings[3]);
                                }

                                Coordinate adjacentCoordRight = new Coordinate(curr.getX() + 1, curr.getY() - 1);

                                String[] stringsAdjRight = mapLayers.get(adjacentCoordRight);

                                if (stringsAdjRight != null) {
                                    result.append(stringsAdjRight[0]);
                                    firstLayerAlreadyPrinted.add(adjacentCoordRight);
                                }
                            }else {
                                Coordinate adjacentCoordLeft = new Coordinate(curr.getX() - 1, curr.getY() - 1);

                                String[] stringsAdjLeft = mapLayers.get(adjacentCoordLeft);

                                if(stringsAdjLeft == null){
                                    result.append(currStrings[4]);
                                }else{
                                    if(!secondLayerAlreadyPrinted.contains(adjacentCoordLeft)){
                                        result.append(stringsAdjLeft[1]);
                                        secondLayerAlreadyPrinted.add(adjacentCoordLeft);
                                    }
                                    result.append(currStrings[4]);
                                }

                                Coordinate adjacentCoordRight = new Coordinate(curr.getX() + 1, curr.getY() - 1);

                                String[] stringsAdjRight = mapLayers.get(adjacentCoordRight);

                                if (stringsAdjRight != null) {
                                    result.append(stringsAdjRight[1]);
                                    secondLayerAlreadyPrinted.add(adjacentCoordRight);

                                }
                            }
                        }

                    }

                }

                result.append("\n");
            }
            startingK = 2;
        }


        return result.toString();

    }
}
