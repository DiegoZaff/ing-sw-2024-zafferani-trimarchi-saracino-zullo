package it.polimi.ingsw.gc28.model.cards;

import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Table;
import it.polimi.ingsw.gc28.model.resources.Resource;
import it.polimi.ingsw.gc28.model.Vertex;
import it.polimi.ingsw.gc28.model.resources.ResourcePrimary;
import javafx.scene.control.Tab;

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
    public  void PlayFront(Table table, Coordinate plaCoordinate){

    }

    @Override
    public void PlayBack(Table table, Coordinate playCoordinate){

    }
}


