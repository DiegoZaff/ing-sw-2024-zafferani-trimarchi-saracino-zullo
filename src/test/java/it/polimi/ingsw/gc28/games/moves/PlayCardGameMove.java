package it.polimi.ingsw.gc28.games.moves;

import it.polimi.ingsw.gc28.games.assertions.GameAssertion;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.errors.types.PlayerActionError;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.fail;

public class PlayCardGameMove extends Move{

    private final CardGame card;

    private final Coordinate coord;


    private final Boolean isFront;


    public PlayCardGameMove(String player, Boolean isFront, CardGame card, Coordinate coord, ArrayList<GameAssertion> assertions) throws IllegalArgumentException {
        super(player, assertions);
        if(card == null || coord == null || assertions == null){
            throw  new IllegalArgumentException();
        }
        this.card = card;
        this.coord = coord;
        this.isFront = isFront;
    }

    /**
     * This constructor is for playing CardInitial
     */
    public PlayCardGameMove(String player, Boolean isFront, CardGame card, ArrayList<GameAssertion> assertions) throws IllegalArgumentException {
        super(player, assertions);
        this.card = card;
        this.coord = new Coordinate(0,0);
        this.isFront = isFront;
    }

    @Override
    public void play(Game game) throws PlayerActionError {

        game.playGameCard(super.getPlayerNick(), card, isFront, coord);

    }
}
