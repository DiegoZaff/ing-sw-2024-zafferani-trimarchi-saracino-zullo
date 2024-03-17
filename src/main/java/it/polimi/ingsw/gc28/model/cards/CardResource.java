package it.polimi.ingsw.gc28.model.cards;

import it.polimi.ingsw.gc28.model.Cell;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Table;
import it.polimi.ingsw.gc28.model.resources.ResourcePrimary;
import it.polimi.ingsw.gc28.model.Vertex;

import java.util.Optional;

public class CardResource extends CardGame {
    private ResourcePrimary resource;
    private int pointsPerPlay;
    public CardResource(Vertex[] verticesFront, ResourcePrimary resource, int pointsPerPlay){
        super(verticesFront);
        this.pointsPerPlay = pointsPerPlay;
        this.resource = resource;

    }

    @Override
    public Optional<ResourcePrimary> getObjectiveResource() {
        return Optional.of(resource);
    }

    @Override
    public  void PlayFront(Table table, Coordinate playCoordinate){

        Cell cell = new Cell(this, 0, true);
        table.AddMapPosition(playCoordinate, cell);


    }

    @Override
    public void PlayBack(Table table, Coordinate playCoordinate){
        Cell cell = new Cell(this, 0, false);
        //da controllare orderPlay, per ora messo a zero
        table.AddMapPosition(playCoordinate, cell);
    }

}

