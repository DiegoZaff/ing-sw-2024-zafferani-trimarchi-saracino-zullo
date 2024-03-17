package it.polimi.ingsw.gc28.model.cards;

import it.polimi.ingsw.gc28.model.Cell;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Table;
import it.polimi.ingsw.gc28.model.resources.Resource;
import it.polimi.ingsw.gc28.model.Vertex;
import it.polimi.ingsw.gc28.model.resources.ResourcePrimary;

import java.util.Optional;

public class CardInitial extends CardGame {
    private Vertex[] verticesBack;
    private Resource[] centralResources;
    public CardInitial(Vertex[] verticesFront, Vertex[] verticesBack, Resource[] centralResources){
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
}


