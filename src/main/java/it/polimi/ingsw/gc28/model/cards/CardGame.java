package it.polimi.ingsw.gc28.model.cards;

import it.polimi.ingsw.gc28.model.Cell;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Table;
import it.polimi.ingsw.gc28.model.Vertex;
import it.polimi.ingsw.gc28.model.resources.Resource;
import it.polimi.ingsw.gc28.model.resources.ResourcePrimary;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class CardGame extends Card {
    private Vertex[] verticesFront;






    public CardGame(Vertex[] verticesFront){

        this.verticesFront = verticesFront;
    }

    /**
     * This method provides the resource primary of the card for counting the points
     * of the position objectives. Its implementation will return Null inside InitialCard.
     */
    public abstract Optional<ResourcePrimary> getObjectiveResource();

    // ? maybe we can merge playFront and playBack by passing an attribute isFront, since
    // ? some of their logic is overlapping. let's see if there's a lot of overlap.

    /**
    * this method play the card in the front verse
    * @param table indicates the table in which the card is played*
    * @param playCoordinate indicate the cordinates in which the card should be played
     */
    public abstract void playFront(Table table, Coordinate playCoordinate);
        // serve per forz aoverride perche il front differisc ela giocabilità tra risorsa e oro


    /**
     * this method play the card in the back verse
     * @param table indicates the table in which the card is played
     * @param playCoordinate indicate the cordinates in which the card should be played
     */
    public abstract void playBack(Table table, Coordinate playCoordinate);
        // credo si possa implementare gia da qui dato che non c'è differenza di giocabilità dei retri

    public Optional<Resource> getResourceInsideVertex (int value){

        return verticesFront[value].getResource();
    }

    /**
     * this method provides a map that contains the number of resource in the vertex in the front of the card
     * @return  Map<Resource, Integer> Integer: the number of a resource in a card
     */
    public Map<Resource,Integer> getFrontCardVertexResource (){
        Map<Resource,Integer> mapResource = new HashMap<>();
        int temp;
        for (Vertex v : verticesFront){
            if (v.isExists()){
                if (v.getResource().isPresent()){
                    if (!mapResource.containsKey(v.getResource().get())){
                        mapResource.put(v.getResource().get(), 1);
                    }else {
                        temp = mapResource.get(v.getResource().get()) +1;
                        mapResource.replace(v.getResource().get(), temp);
                    }
                }
            }
        }
        return mapResource;
    }

    public abstract int getPoints();


    /**
     * this method return a map with the central resources of the card
     * @return a map <Resource, Integer><the resource/ the numeber of that resource>
     */
    public abstract Map<Resource,Integer> getMapCentralResource();


}

