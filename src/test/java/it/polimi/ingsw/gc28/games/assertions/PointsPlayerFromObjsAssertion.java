package it.polimi.ingsw.gc28.games.assertions;

import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.model.Player;

import java.util.Optional;

public class PointsPlayerFromObjsAssertion  extends GameAssertion{

    private final String playerNick;

    private final int points;

    private Integer actualPoints;

    public PointsPlayerFromObjsAssertion(String playerNick, int points) {
        this.playerNick = playerNick;
        this.points = points;
    }


    @Override
    public boolean verifyAssertion(Game game) {
        Optional<Player> player =  super.getPlayerFromNick(playerNick, game);

        if(player.isEmpty()){
            actualPoints = null;
            return false;
        }

        actualPoints = player.get().getObjectivePoints();

        return points == actualPoints;
    }

    @Override
    public String toString() {
        return String.format("PointsPlayerFromObjsAssertion --- expectedPoints: %d, actual: %d", points, actualPoints);
    }
}
