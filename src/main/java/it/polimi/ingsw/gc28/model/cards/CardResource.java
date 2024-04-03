package it.polimi.ingsw.gc28.model.cards;

import it.polimi.ingsw.gc28.model.Cell;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Table;
import it.polimi.ingsw.gc28.model.Vertex;
import it.polimi.ingsw.gc28.model.resources.Resource;
import it.polimi.ingsw.gc28.model.resources.ResourcePrimary;
import it.polimi.ingsw.gc28.model.resources.utils.ResourcePrimaryType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CardResource extends CardGame {
    private ResourcePrimary resource;
    private int pointsPerPlay;

    public CardResource(ResourceType[] resourceCard, ResourcePrimaryType resource, int pointsPerPlay){
        super(resourceCard);
        this.pointsPerPlay = pointsPerPlay;
        this.resource = new ResourcePrimary(resource);

    }

    @Override
    public Optional<ResourcePrimary> getObjectiveResource() {
        return Optional.of(resource);
    }

    @Override
    public void playFront(Table table, Coordinate playCoordinate){

        Cell cell = new Cell(this, 0, true);
        table.addMapPosition(playCoordinate, cell);
        table.removePlayableCoordinate(playCoordinate);

        // orderplay è zero alla prima carta piazzata, dopodichè incrementa ad ogni carta piazzata.
        // Idea: creare attributo lastOrderPlayNumber che è attributo di Table e nel creare la cella si fa get e +1


    }

    // questi due metodi si occupano per ora solo di piazzare la carta in MapPositions

    @Override
    public void playBack(Table table, Coordinate playCoordinate){
        Cell cell = new Cell(this, 0, false);
        //da controllare orderPlay, per ora messo a zero
        table.addMapPosition(playCoordinate, cell);
        table.removePlayableCoordinate(playCoordinate);
    }

    @Override
    public int getPoints(Table table, Coordinate coordinate){
        return pointsPerPlay;
    }

    @Override
    public Map<Resource,Integer> getBackCardResource(){
        Map<Resource, Integer> m = new HashMap<>();
        m.put(resource, 1);
        return m;
    }

    @Override
    public Vertex[] getVerticesBack(){
        Vertex[] verticesBack = new Vertex[4];
        Vertex v = new Vertex(true);
        for (int i = 0; i<= 3; i++){
            verticesBack[i] = v;
        }
        return verticesBack;
    }

}

