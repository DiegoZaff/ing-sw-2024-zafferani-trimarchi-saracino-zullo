package it.polimi.ingsw.gc28.games.assertions;

import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.model.Player;

import java.util.Optional;

public abstract class GameAssertion {

    public abstract boolean verifyAssertion(Game game);

    public abstract String toString();

    public Optional<Player> getPlayerFromNick(String playerNick, Game game){
        return game.getPlayers().stream().filter(p -> p.getName().equals(playerNick)).findFirst();
    }
}
