package it.polimi.ingsw.gc28.games.moves;

import it.polimi.ingsw.gc28.games.assertions.GameAssertion;
import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.model.cards.CardObjective;
import it.polimi.ingsw.gc28.model.errors.PlayerActionError;
import it.polimi.ingsw.gc28.model.utils.PlayerColor;

import java.util.ArrayList;

public class ChooseColorMove extends Move{

    private final String color;


    public ChooseColorMove(String playerNick, ArrayList<GameAssertion> assertions, String color) throws IllegalArgumentException {
        super(playerNick, assertions);
        if (color == null) {
            throw new IllegalArgumentException();
        }
        this.color = color;
    }


    @Override
    public void play(Game game) throws PlayerActionError {
        game.chooseColor(super.getPlayerNick(), color);
    }
}


