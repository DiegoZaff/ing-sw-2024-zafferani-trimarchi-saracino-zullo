package it.polimi.ingsw.gc28.games.moves;

import it.polimi.ingsw.gc28.games.assertions.GameAssertion;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.model.cards.*;
import it.polimi.ingsw.gc28.model.errors.types.PlayerActionError;

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

    public String getPlayerNick(){
        return this.playerNick;
    }


    public ArrayList<GameAssertion> getAssertions() {
        return assertions;
    }

    public abstract void play(Game game) throws PlayerActionError;

    public static Move createMove(String player, ActionType action, Boolean isFront, CardResource cardRes, CardGold cardGold, CardInitial cardInitial, CardObjective cardObj, Coordinate coord, Boolean fromGoldDeck, ArrayList<GameAssertion> gameAssertions) throws IllegalArgumentException{
        if(action.equals(ActionType.JOIN_GAME)){
            return new JoinGameMove(player, gameAssertions);
        } else if (action.equals(ActionType.PLAY_INITIAL_CARD)) {
            return new PlayCardGameMove(player, isFront, cardInitial, gameAssertions);
        } else if (action.equals(ActionType.PLAY_CARD)) {
            if(cardRes != null) {
                return new PlayCardGameMove(player, isFront, cardRes, coord, gameAssertions);
            }
            return new PlayCardGameMove(player, isFront, cardGold, coord, gameAssertions);

        } else if (action.equals(ActionType.DRAW_CARD)) {
            if (fromGoldDeck != null) {
                return new DrawCardMove(player, gameAssertions, fromGoldDeck);
            }
            if(cardRes != null) {
                return new DrawCardMove(player, gameAssertions, cardRes);
            }
            return new DrawCardMove(player, gameAssertions, cardGold);
        } else if (action.equals(ActionType.CHOOSE_OBJ)) {
            return new ChooseObjectiveMove(player, gameAssertions, cardObj);
        } else  {
            return new NoMove(player, gameAssertions);
        }
    }

}
