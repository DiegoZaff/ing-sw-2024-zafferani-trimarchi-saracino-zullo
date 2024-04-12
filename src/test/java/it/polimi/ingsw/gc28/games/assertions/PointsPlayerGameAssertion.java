package it.polimi.ingsw.gc28.games.assertions;

import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.model.Player;

import java.util.Optional;

public class PointsPlayerGameAssertion extends GameAssertion{

    private final int points;
    private int actualPoints;

    private final String nickname;

    public PointsPlayerGameAssertion(int points, String nickname) {
        this.points = points;
        this.nickname = nickname;
    }

    @Override
    public boolean verifyAssertion(Game game) {
        Optional<Player> player = game.getPlayers().stream().filter((p) -> p.getName().equals(nickname)).findFirst();

        if(player.isEmpty()){
            System.err.println("Non existent player");
            return false;
        }

        actualPoints = player.get().getPoints();
        return actualPoints == this.points;
    }

    @Override
    public String toString() {
        return String.format("PointsPlayerGameAssertion --- expectedPoints: %d, actual: %d", points, actualPoints);
    }
}
