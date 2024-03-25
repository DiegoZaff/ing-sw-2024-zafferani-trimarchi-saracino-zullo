package it.polimi.ingsw.gc28.model.cards;

import it.polimi.ingsw.gc28.model.Cell;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Table;
import it.polimi.ingsw.gc28.model.Vertex;
import it.polimi.ingsw.gc28.model.resources.Resource;
import it.polimi.ingsw.gc28.model.resources.ResourcePrimary;
import it.polimi.ingsw.gc28.model.resources.ResourceSpecial;
import it.polimi.ingsw.gc28.model.resources.utils.ResourcePrimaryType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceSpecialType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class CardGame extends Card {
    private final Vertex[] verticesFront;
    private final Resource[] resourcesFront;


    public CardGame(ResourceType[] resourcesFront){

        Resource[] resourceInCard = new Resource[3];
        Vertex[] vertices = new Vertex[3];
        int[] hasVertex = new int[3];

        for (int i=0; i<resourcesFront.length; i++) {
            hasVertex[i] = 0;
            if(resourcesFront[i] == ResourceType.FOX) {
                resourceInCard[i] = new ResourcePrimary(ResourcePrimaryType.FOX);
            } else if (resourcesFront[i] == ResourceType.LEAF) {
                resourceInCard[i] = new ResourcePrimary(ResourcePrimaryType.LEAF);
            } else if (resourcesFront[i] == ResourceType.BUTTERFLY) {
                resourceInCard[i] = new ResourcePrimary(ResourcePrimaryType.BUTTERFLY);
            } else if (resourcesFront[i] == ResourceType.MUSHROOM) {
                resourceInCard[i] = new ResourcePrimary(ResourcePrimaryType.MUSHROOM);
            } else if (resourcesFront[i] == ResourceType.FEATHER) {
                resourceInCard[i] = new ResourceSpecial(ResourceSpecialType.FEATHER);
            } else if (resourcesFront[i] == ResourceType.PARCHMENT) {
                resourceInCard[i] = new ResourceSpecial(ResourceSpecialType.PARCHMENT);
            } else if (resourcesFront[i] == ResourceType.POTION) {
                resourceInCard[i] = new ResourceSpecial(ResourceSpecialType.POTION);
            } else if (resourcesFront[i] == ResourceType.noResource) {
                resourceInCard[i] = null;
                hasVertex[i] = 1;
            } else {
                resourceInCard[i] = null;
                hasVertex[i] = 2;
            }
        }

        for (int i=0; i< resourceInCard.length; i++){
            if(hasVertex[i] == 0) {
                vertices[i] = new Vertex(resourceInCard[i]);
            }
            else if(hasVertex[i] == 1){
                vertices[i] = new Vertex(true);
            }
            else vertices[i] = new Vertex(false);
        }
        this.verticesFront = vertices;
        this.resourcesFront = resourceInCard;
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

    public abstract int getPoints(Table table, Coordinate coordinate);


    /**
     * this method return a map with the central resources of the card
     * @return a map <Resource, Integer><the resource/ the numeber of that resource>
     */
    public abstract Map<Resource,Integer> getMapCentralResource();


}

