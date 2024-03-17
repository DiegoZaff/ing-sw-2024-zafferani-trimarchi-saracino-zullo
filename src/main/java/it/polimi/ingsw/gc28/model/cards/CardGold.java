package it.polimi.ingsw.gc28.model.cards;

import it.polimi.ingsw.gc28.model.Challenge;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Table;
import it.polimi.ingsw.gc28.model.resources.Resource;
import it.polimi.ingsw.gc28.model.resources.ResourcePrimary;
import it.polimi.ingsw.gc28.model.Vertex;

import java.util.Map;


public class CardGold extends CardResource {
    private Map<Resource, Integer> playability;
    private Challenge challenge;

    public CardGold(Vertex[] verticesFront, ResourcePrimary resource, int pointsPerPlay, Map<Resource, Integer> playability, Challenge challenge){
        super(verticesFront, resource, pointsPerPlay);
        this.playability = playability;
        this.challenge = challenge;
    }

    @Override
    public void playFront(Table table, Coordinate playCoordinate){
        //come metodo super ma controllando se giocabile
    }


}