package it.polimi.ingsw.gc28.model.actions;

import it.polimi.ingsw.gc28.model.errors.ErrorManager;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.actions.utils.ActionType;

import java.util.ArrayList;

/**
 * This class manages the playerOfTurn and actionType lifecycle.
 * It's responsible for updating them accordingly so that it simplifies the
 * Game Class considerably
 */
public class ActionManager {

    private ArrayList<Player> players;

    private Player playerOfTurn;

    public Player getPlayerOfTurn(){
        return  playerOfTurn;
    }

    private ActionType type;

    public ActionType getActionType(){
        return type;
    }

    /**
     * Initialize first Action.
     * @param players must be of length > 0
     */
    public ActionManager(ArrayList<Player> players){
        this.players = players;
        this.type = ActionType.CHOOSE_OBJ;
        playerOfTurn = players.getFirst();
    }


    /**
     * This method checks weather player 'p' can perform action 'a'.
     * @param p is the player that wants to do something
     * @param a is the action that 'p' wants to perform
     * @return true is it is the expected action from the expected player.
     */
    public boolean validatesMove(Player p, ActionType a){
        return playerOfTurn.equals(p) && a.equals(type);
    }

    /**
     * This method calculates the next expected actionType and playerOfTurn based
     * on the current ones.
     */
    public void nextMove(){
        int indexOfCurr = players.indexOf(playerOfTurn);
        switch (type){
            case CHOOSE_OBJ -> {
                if(indexOfCurr ==  players.size() - 1){
                    type = ActionType.PLAY_INITIAL_CARD;
                }
                playerOfTurn = getNextPlayer();
            }
            case PLAY_INITIAL_CARD -> {
                type = ActionType.PLAY_CARD;
            }
            case PLAY_CARD -> {
                type = ActionType.DRAW_CARD;
            }
            case DRAW_CARD -> {
                type = ActionType.PLAY_CARD;
                playerOfTurn = getNextPlayer();
            }
        }
    }

    /**
     * Helper method
     * @return the next player in the order of play.
     */
    private Player getNextPlayer(){
        int indexOfCurr = players.indexOf(playerOfTurn);

        return players.get((indexOfCurr + 1) % players.size());
    }

    /**
     * This will signal the end of the game. No more moves will be validated.
     */
    public void gameFinished(){
        type = ActionType.GAME_ENDED;
        playerOfTurn = getNextPlayer();
    }
}
