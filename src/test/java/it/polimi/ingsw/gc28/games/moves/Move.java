package it.polimi.ingsw.gc28.games.moves;

import it.polimi.ingsw.gc28.games.assertions.GameAssertion;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.cards.CardObjective;

import java.util.ArrayList;
import java.util.Optional;

public abstract class Move {
    private final String playerNick;

    private final ArrayList<GameAssertion> assertions;

    public Move(String playerNick, ArrayList<GameAssertion> assertions) throws IllegalArgumentException {
        if(playerNick == null){
            throw new IllegalArgumentException();
        }

        this.playerNick = playerNick;
        this.assertions = assertions;
    }


    public Optional<Player> getPlayer(Game game){
        return game.getPlayers().stream().filter(p -> p.getName().equals(playerNick)).findFirst();
    }


    public ArrayList<GameAssertion> getAssertions() {
        return assertions;
    }

    public abstract void play(Game game);

    public static Move createMove(String player,ActionType action, Boolean isFront,CardGame card, CardObjective cardObj,Coordinate coord, Boolean fromGoldDeck,ArrayList<GameAssertion> gameAssertions) throws IllegalArgumentException{
        if (action == ActionType.PLAY_INITIAL_CARD) {
            return new PlayCardGameMove(player, isFront, card, gameAssertions);
        } else if (action == ActionType.PLAY_CARD) {
            return new PlayCardGameMove(player, isFront, card, coord, gameAssertions);
        } else if (action == ActionType.DRAW_CARD) {
            if (fromGoldDeck != null) {
                return new DrawCardMove(player, gameAssertions, fromGoldDeck);
            }
            return new DrawCardMove(player, gameAssertions, card);
        } else if (action == ActionType.CHOOSE_OBJ) {
            return new ChooseObjectiveMove(player, gameAssertions, cardObj);
        } else  {
            return new NoMove(player, gameAssertions);
        }
    }

}
