package it.polimi.ingsw.gc28.model.cards;

import it.polimi.ingsw.gc28.model.Cell;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Table;
import it.polimi.ingsw.gc28.model.resources.Resource;
import it.polimi.ingsw.gc28.model.Vertex;
import it.polimi.ingsw.gc28.model.resources.ResourcePrimary;
import it.polimi.ingsw.gc28.model.resources.utils.ResourcePrimaryType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CardInitial extends CardGame {
    private final Resource[] resourcesBack;
    //private final ResourceType[] resourcesFront;
    private final Vertex[] verticesBack;
    private Map<Resource, Integer> centralResources;
    public CardInitial(ResourceType[] resourcesFront, ResourceType[] resourcesBack, ResourceType[] primaryResources){
        super(resourcesFront);

        Vertex[] vertexBack = new Vertex[4];
        Resource[] resBack = new Resource[4];

        for (int i=0; i<resourcesBack.length; i++) {
            if(resourcesBack[i] == ResourceType.FOX) {
                resBack[i] = new ResourcePrimary(ResourcePrimaryType.FOX);
            } else if (resourcesBack[i] == ResourceType.LEAF) {
                resBack[i] = new ResourcePrimary(ResourcePrimaryType.LEAF);
            } else if (resourcesBack[i] == ResourceType.BUTTERFLY) {
                resBack[i] = new ResourcePrimary(ResourcePrimaryType.BUTTERFLY);
            } else if (resourcesBack[i] == ResourceType.MUSHROOM) {
                resBack[i] = new ResourcePrimary(ResourcePrimaryType.MUSHROOM);
            } else {
                resBack[i] = null;
            }
        }

        for (int i=0; i< resBack.length; i++) {
            if(resBack[i]!=null) {
                vertexBack[i] = new Vertex(resBack[i]);
            }
        }
        this.verticesBack=vertexBack;
        this.resourcesBack =resBack;

        Resource[] resCenter = new Resource[3];
        for (int i=0; i<primaryResources.length; i++) {
            if (primaryResources[i] == ResourceType.FOX) {
                resCenter[i] = new ResourcePrimary(ResourcePrimaryType.FOX);
            } else if (primaryResources[i] == ResourceType.LEAF) {
                resCenter[i] = new ResourcePrimary(ResourcePrimaryType.LEAF);
            } else if (primaryResources[i] == ResourceType.BUTTERFLY) {
                resCenter[i] = new ResourcePrimary(ResourcePrimaryType.BUTTERFLY);
            } else if (primaryResources[i] == ResourceType.MUSHROOM) {
                resCenter[i] = new ResourcePrimary(ResourcePrimaryType.MUSHROOM);
            } else {
                resCenter[i] = null;
            }
        }
        centralResources = new HashMap<>();
        createCentralResources(centralResources, resCenter);
    }

    public void createCentralResources(Map<Resource, Integer> centralResources, Resource[] Resources){
        for(Resource resource: Resources){
            centralResources.put(resource, 1);
        }
    }


    @Override
    public Optional<ResourcePrimary> getObjectiveResource() {
        return Optional.empty();
    }

    @Override
    public void playFront(Table table, Coordinate playCoordinate){
        Cell cell = new Cell(this, 0, true);
        table.addMapPosition(playCoordinate, cell);
        table.removePlayableCoordinate(playCoordinate);
    }

    //potremmo implementare playFront e playBack in CardGame, e fare override solo in CardGold

    @Override
    public void playBack(Table table, Coordinate playCoordinate){
        Cell cell = new Cell(this, 0, false);
        table.addMapPosition(playCoordinate, cell);
        table.removePlayableCoordinate(playCoordinate);
    }

    /**
     * this method provides a map that contains the number of resource in the vertex in the back of the card
     * @return  Map<Resource, Integer> Integer: the number of a resource in a card
     */
    public Map<Resource,Integer> getBackCardVertexResource (){
        Map<Resource,Integer> mapResource = new HashMap<>();
        int temp;
        for (Vertex v : verticesBack){
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

    @Override
    public int getPoints(Table table, Coordinate coordinate){
        return 0;
    }

    @Override
    public Map<Resource,Integer> getMapCentralResource(){
        return centralResources;
    }

    @Override
    public Vertex[] getVerticesBack() {
        return verticesBack;
    }
}


