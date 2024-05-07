package it.polimi.ingsw.gc28.model.challenge;

import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Table;
import it.polimi.ingsw.gc28.model.challenge.utils.ChallengeType;

import java.io.Serializable;

public class PositionChallenge extends Challenge implements Serializable {
    public PositionChallenge(ChallengeType type){
        super(type);
    }

    private int countAdjacent(Table table, Coordinate coordinate){
        int i = 0;
        if (table.CellNWPresent(coordinate)){
            i++;
        }
        if (table.CellNEPresent(coordinate)){
            i++;
        }
        if (table.CellSEPresent(coordinate)){
            i++;
        }
        if (table.CellSWPresent(coordinate)){
            i++;
        }
        return i;
    }

    @Override
    public int challengePoints(Table table, Coordinate coordinate){
        return this.countAdjacent(table, coordinate) * 2;
    }
}
