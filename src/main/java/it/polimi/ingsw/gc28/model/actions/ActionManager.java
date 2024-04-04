package it.polimi.ingsw.gc28.model.actions;

import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.model.errors.ErrorManager;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class manages the playerOfTurn and actionType lifecycle.
 * It's responsible for updating them accordingly so that it simplifies the
 * Game Class considerably
 */
public class ActionManager {

    private ArrayList<Player> players;

    private Player playerOfTurn;

    private final ErrorManager errorManager;

    public Player getPlayerOfTurn(){
        return  playerOfTurn;
    }

    private ActionType actionType;

    public ActionType getActionType(){
        return actionType;
    }


    /**
     * Initialize first Action.
     * @param players must be of length > 0
     */
    public ActionManager(ArrayList<Player> players, ErrorManager errorManager){
        this.players = players;
        this.errorManager = errorManager;
        this.actionType = ActionType.CHOOSE_OBJ;
        playerOfTurn = players.getFirst();
    }


    /**
     * This method checks weather player 'p' can perform action 'a'.
     * @param p is the player that wants to do something
     * @param a is the action that 'p' wants to perform
     * @return true is it is the expected action from the expected player.
     */
    public boolean validatesMove(Player p, ActionType a){

        // when actionType is CHOOSE_OBJ check is different.
        if(actionType == ActionType.CHOOSE_OBJ ){
            if (a.equals(ActionType.CHOOSE_OBJ) && p.getObjectiveChosen().isEmpty()){
                return true;
            }else{
                errorManager.fromWrongMove(p, a, this);
                return false;
            }

        }

        if(playerOfTurn.equals(p) && a.equals(actionType)){
            return true;
        }else{
            errorManager.fromWrongMove(p, a, this);
            return false;
        }



    }

    /**
     * This method calculates the next expected actionType and playerOfTurn based
     * on the current ones.
     */
    public void nextMove(){
        switch (actionType){
            case CHOOSE_OBJ -> {
                int numberOfPlayers = players.size();

                int numbersOfPlayersWithObjective = players.stream().map(Player::getObjectiveChosen)
                        .filter(Optional::isPresent)
                        .collect(Collectors.toCollection(ArrayList::new))
                        .size();

                if(numbersOfPlayersWithObjective == numberOfPlayers){
                    actionType = ActionType.PLAY_INITIAL_CARD;
                }
            }
            case PLAY_INITIAL_CARD -> actionType = ActionType.PLAY_CARD;
            case PLAY_CARD -> actionType = ActionType.DRAW_CARD;
            case DRAW_CARD -> {
                actionType = ActionType.PLAY_CARD;
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
        actionType = ActionType.GAME_ENDED;
        playerOfTurn = getNextPlayer();
    }
}
