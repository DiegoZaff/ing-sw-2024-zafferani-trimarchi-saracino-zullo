package it.polimi.ingsw.gc28.model.challenge;

import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Table;
import it.polimi.ingsw.gc28.model.challenge.utils.ChallengeType;

import java.io.Serializable;

public class PositionChallenge extends Challenge implements Serializable {
    public PositionChallenge(ChallengeType type){
        super(type);
    }

    /**
     * this method counts how many vertices it covers, it is used for the covered vertices challenge
     * @param table of the player
     * @param coordinate from where to check whether it covers any vertices
     * @return
     */
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

    /**
     * returns the points to award for the challenge, 2 points for every corner covered
     * @param table
     * @param coordinate
     * @return
     */
    @Override
    public int challengePoints(Table table, Coordinate coordinate){
        return this.countAdjacent(table, coordinate) * 2;
    }

    @Override
    public String toString(){
        return "2/cor";
    }
}
