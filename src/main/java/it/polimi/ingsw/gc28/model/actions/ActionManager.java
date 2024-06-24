package it.polimi.ingsw.gc28.model.actions;

import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.model.errors.types.AlreadyChoseObjectiveError;
import it.polimi.ingsw.gc28.model.errors.types.NotYourTurnError;
import it.polimi.ingsw.gc28.model.errors.PlayerActionError;
import it.polimi.ingsw.gc28.model.errors.types.UnexpectedMoveError;
import it.polimi.ingsw.gc28.model.errors.types.UnrestorableGameError;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

/**
 * This class manages the playerOfTurn and actionType lifecycle.
 * It's responsible for updating them accordingly so that it simplifies the
 * Game Class considerably
 */
public class ActionManager implements Serializable {

    final private int nPlayers;

    private ArrayList<Player> players;

    private Player playerOfTurn;
    private Player firstPlayer;


    private Integer indexFirstPlayer;

    public Optional<Player> getPlayerOfTurn(){
        return Optional.ofNullable(playerOfTurn);
    }

    private ActionType actionType;

    private ActionType savedAction;


    /**
     * This attribute is null until a player reaches 20 points, counting
     * the number of rounds left to play. (it could be less if the deck finishes
     * the cards).
     */
    private Integer roundsLeft;

    public Optional<Integer> getRoundsLeft(){
        return Optional.ofNullable(roundsLeft);
    }

    public ActionType getActionType(){
        return actionType;
    }


    /**
     * Initialize first Action.
     * @param players must be of length > 0
     */
    public ActionManager(int nPlayers, ArrayList<Player> players, Integer i){
        this.nPlayers = nPlayers;
        this.players = players;
        this.actionType = ActionType.JOIN_GAME;
        this.indexFirstPlayer = i;
    }


    public ActionManager(int nPlayers, ArrayList<Player> players){
        this.nPlayers = nPlayers;
        this.players = players;
        this.actionType = ActionType.JOIN_GAME;
    }


    /**
     * This method checks weather player 'p' can perform action 'a'.
     * @param p is the player that wants to do something
     * @param a is the action that 'p' wants to perform
     * @return true is it is the expected action from the expected player.
     */
    public void validatesMove(Player p, ActionType a) throws PlayerActionError {

        // when actionType is CHOOSE_OBJ check is different.
        if(actionType == ActionType.CHOOSE_OBJ ){
            if (!a.equals(ActionType.CHOOSE_OBJ) || p.getObjectiveChosen().isPresent()) {
                if(!a.equals(ActionType.CHOOSE_OBJ)){
                    throw new NotYourTurnError();
                }else if(p.getObjectiveChosen().isPresent()){
                    throw new AlreadyChoseObjectiveError();
                }
            }

        }

        if (!playerOfTurn.equals(p) || !a.equals(actionType)) {
            if(!p.equals(playerOfTurn)){
                throw new NotYourTurnError();
            }else if(!a.equals(actionType)){
                throw new UnexpectedMoveError();
            }
        }
    }

    /**
     * this method checks if the current player is the last player to perform the action
     * @return boolean
     */
    private boolean isCurrentPlayerTheLastOneForTheAction(){

        int lastPlayerIndex = ((indexFirstPlayer - 1) % players.size());

        if (lastPlayerIndex < 0) {
            lastPlayerIndex += players.size();
        }
        return players.indexOf(playerOfTurn) == lastPlayerIndex;
    }

    /**
     * This method calculates the next expected actionType and playerOfTurn based
     * on the current ones.
     */
    public void nextMove(){


        switch (actionType){
            case JOIN_GAME -> {
                if(players.size() == nPlayers){
                    actionType = ActionType.CHOOSE_COLOR;
                }
            }
            case CHOOSE_COLOR -> {
                if(isCurrentPlayerTheLastOneForTheAction()) {

                    actionType = ActionType.PLAY_INITIAL_CARD;
                    playerOfTurn = getFirstPlayer();
                    break;
                }
                playerOfTurn = getNextPlayer();
            }
            case PLAY_INITIAL_CARD -> {
                if(isCurrentPlayerTheLastOneForTheAction()) {


                    actionType = ActionType.CHOOSE_OBJ;
                    playerOfTurn = getFirstPlayer();
                    break;
                }
                playerOfTurn = getNextPlayer();

            }
            case CHOOSE_OBJ -> {
                if(isCurrentPlayerTheLastOneForTheAction()) {


                    actionType = ActionType.PLAY_CARD;
                    playerOfTurn = getFirstPlayer();
                    break;
                }
                playerOfTurn = getNextPlayer();

            }
            case PLAY_CARD -> {
                // TODO DONE : if roundsLeft <= numberOfPlayers - 1 => actionType = PLAY_CARD & nextPlayer aggiornato
                if(getRoundsLeft().isPresent() && roundsLeft <= nPlayers){
                    // actionType remains PLAY_CARD
                    playerOfTurn = getNextPlayer();
                    updateRoundsLeft();
                }else{
                    if(getRoundsLeft().isPresent()){
                        updateRoundsLeft();
                    }
                    actionType = ActionType.DRAW_CARD;
                }
            }
            case DRAW_CARD -> {
                actionType = ActionType.PLAY_CARD;
                playerOfTurn = getNextPlayer();
                updateRoundsLeft();
            }
            case  WAIT_RECONNECTIONS -> {
                boolean isAllReconnected = players.stream().allMatch(Player::isConnected);

                if(isAllReconnected){
                    if(savedAction != null){
                        actionType = savedAction;
                    }else{
                        // should not happen
                        throw  new RuntimeException();
                    }

                }
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
     * This method chooses randomly the first player.
     */
    public void initFirstPlayer(){
        if(indexFirstPlayer == null){

            Random rand = new Random();
            indexFirstPlayer = rand.nextInt(players.size());
        }
        this.firstPlayer = players.get(indexFirstPlayer);
    }

    public void initPlayerOfTurn(){             //aggiunta inizializzazione playerOfTurn
        this.playerOfTurn = firstPlayer;
    }

    public Player getFirstPlayer(){
        return firstPlayer;
    }

    public void updateRoundsLeft(){
        if(getRoundsLeft().isPresent() && roundsLeft > 0){
            roundsLeft -= 1;

            if(roundsLeft == 0){
                actionType = ActionType.GAME_ENDED;
            }
        }
    }

    /**
     * this method gets called when a player has reached 20 points
     * or there are no more cards to be drawn
     */
    public void initRoundsLeft(){

        int roundsToFinishCircle = ((indexFirstPlayer - players.indexOf(playerOfTurn)) % players.size()) * 2 + 1 + players.size();

        int additionalCircle = players.size();

        roundsLeft = additionalCircle + roundsToFinishCircle;
    }


    public void setWaitForReconnections() throws UnrestorableGameError {
        if(actionType == null){
            // looks like game never started in the first place.
            throw new UnrestorableGameError(actionType);
        }

        if(!actionType.equals(ActionType.WAIT_RECONNECTIONS)){
            savedAction = actionType;
        }else if(savedAction == null){
            // this is weird and should never happen.
            throw new UnrestorableGameError(savedAction);

        }
        // else branch: game crashed after crashing, start reconnection process again

        for(Player p : players){
            p.setConnected(false);
        }

        actionType = ActionType.WAIT_RECONNECTIONS;
    }
}
