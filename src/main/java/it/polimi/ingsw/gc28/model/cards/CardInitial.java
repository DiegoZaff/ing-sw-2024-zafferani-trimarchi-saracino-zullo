package it.polimi.ingsw.gc28.model.cards;

import it.polimi.ingsw.gc28.model.Cell;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Table;
import it.polimi.ingsw.gc28.model.resources.Resource;
import it.polimi.ingsw.gc28.model.Vertex;
import it.polimi.ingsw.gc28.model.resources.ResourcePrimary;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CardInitial extends CardGame {
    private Vertex[] verticesBack;
    private Map<Resource, Integer> centralResources;
    public CardInitial(Vertex[] verticesFront, Vertex[] verticesBack, Map<Resource,Integer> centralResources){
        super(verticesFront);
        this.verticesBack = verticesBack;
        this.centralResources = centralResources;

    }

    @Override
    public Optional<ResourcePrimary> getObjectiveResource() {
        return Optional.empty();
    }

    @Override
    public void playFront(Table table, Coordinate playCoordinate){
        Cell cell = new Cell(this, 0, true);
        table.addMapPosition(playCoordinate, cell);
    }

    //potremmo implementare playFront e playBack in CardGame, e fare override solo in CardGold

    @Override
    public void playBack(Table table, Coordinate playCoordinate){
        Cell cell = new Cell(this, 0, false);
        table.addMapPosition(playCoordinate, cell);
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
}


