package it.polimi.ingsw.gc28.games.moves;

import it.polimi.ingsw.gc28.games.assertions.GameAssertion;
import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.cards.CardResource;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.fail;

public class DrawCardMove extends Move{
    private final Boolean fromGoldDeck;

    private final CardResource card;

    // draw from deck
    public DrawCardMove(String playerNick, ArrayList<GameAssertion> assertions, Boolean fromGoldDeck) throws IllegalArgumentException {
        super(playerNick, assertions);
        if(fromGoldDeck == null){
            throw new IllegalArgumentException();
        }
        this.fromGoldDeck = fromGoldDeck;
        this.card = null;
    }

    // draw from visible cards
    public DrawCardMove(String playerNick, ArrayList<GameAssertion> assertions, CardResource card ) throws IllegalArgumentException {
        super(playerNick, assertions);
        if(card == null){
            throw new IllegalArgumentException();
        }
        this.fromGoldDeck = null;
        this.card = card;
    }

    private boolean isDrawFromDeck(){
        return fromGoldDeck != null;
    }

    @Override
    public void play(Game game) {
        Optional<Player> player = super.getPlayer(game);

        if(player.isEmpty()){
            fail("Error initialization of Game");
        }

        if(isDrawFromDeck()){
            game.drawGameCard(player.get(), fromGoldDeck);
        }else{
            game.drawGameCard(player.get(), card);
        }
    }
}
