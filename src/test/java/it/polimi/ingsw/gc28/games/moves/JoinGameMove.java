package it.polimi.ingsw.gc28.games.moves;

import it.polimi.ingsw.gc28.games.assertions.GameAssertion;
import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.model.errors.PlayerActionError;

import java.util.ArrayList;

public class JoinGameMove extends Move{

    public JoinGameMove(String playerNick, ArrayList<GameAssertion> assertions) throws IllegalArgumentException {
        super(playerNick, assertions);
    }

    @Override
    public void play(Game game) throws PlayerActionError {
        game.addPlayerToGame(super.getPlayerNick());
    }
}
