package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.cards.CardInitial;
import it.polimi.ingsw.gc28.model.cards.CardObjective;
import it.polimi.ingsw.gc28.model.objectives.Objective;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class Game {

    enum ActionType{
        PLAY_INITIAL_CARD,
        PLAY_CARD,
        DRAW_CARD,
        CHOOSE_OBJ,
    }

    private ActionType action;

    private Player playerOfTurn;

    private ArrayList<CardObjective> globalObjectives;

    private Deck deck;

    private ArrayList<Player> players;

    /**
     * This attribute is null until a player reaches 20 points, counting
     * the number of rounds left to play. (it could be less if the deck finishes
     * the cards).
     */
    private Optional<Integer> roundsLeft;


    /**
     * This method is responsible for checking if the player who has just
     * placed a card has reached 20 points and in that case it sets up a counter
     * which counts the rounds left.
     */
    private void checkEndGame() {

        boolean has20points = playerOfTurn.getPoints() >= 20;

        if(has20points){
            // index of the player who has just played a card.
            int indexOfPlayerOfTurn = players.indexOf(playerOfTurn);

            // same number of plays + 1 additional round each
            int newRoundsLeft = 2 * players.size() - (indexOfPlayerOfTurn + 1);

            roundsLeft = Optional.of(newRoundsLeft);
        }
    }

    /**
     * This method is called when roundsLeft is set and manages state in rounds
     * after a player has reached 20 points.
     * ! TODO : make it work also when deck is finished and no player has reached 20 points.
     *
     * @param currRoundsLefts rounds left to be played.
     */
    private void endGame(int currRoundsLefts){
        if(currRoundsLefts == 0){
            calculateObjectivePoints();
            calculateWinner();
        }else{
            roundsLeft = Optional.of(currRoundsLefts - 1);
        }
    }


    /**
     * play a card in a player table
     * @param playingPlayer the person who is playing the card
     * @param playedCard the card to be played
     * @param isFront indicate how the card has to be played, front if True, back if False
     * @param coordinates indicates the coordinate where the card has to be played
     */
    private void playGameCard (Player playingPlayer, CardGame playedCard, boolean isFront, Coordinate coordinates ) {
        //illegal coordinates exception?
        // ? maybe let's have an attribute error inside player which stores possible
        // ? errors for the player, which will be reflected in the ui with appropriate error messages.
        // ? (other kinds of errors are for illegal moves, playing when it's another player's turn, etc...)

        if(!validatesMove(playingPlayer, ActionType.PLAY_CARD)){
            // ... check for different kinds of errors and update error attribute inside player
        };

        playingPlayer.playCard(playedCard, isFront, coordinates);

        // ? maybe clean up all players' errors when a move is successful.
        // ? cleanUpAllPlayersErrors();

        // set up attributes for next turn.
        setupNextMove();

    }

    /**
     * This method is responsible for calling '.calculatePoints(objs)' on every player when the game
     * is finished. This is a callback of 'endGame()'
     */
    private void calculateObjectivePoints(){
        for(Player player : players){
            player.calculateObjectivePoints(globalObjectives.stream()
                    .map((CardObjective::getObjective))
                    .collect(Collectors.toCollection(ArrayList::new)));
        }
    }

    /**
     * This method calculates who's the winner at the end of the game.
     * ? maybe add a winner attribute Optional<Player> and set winner player.
     */
    private void calculateWinner(){


        int maxPoints = 0;
        ArrayList<Player> winners = new ArrayList<>();
        ArrayList<Player> winnersAfterObjectivePointsCheck = new ArrayList<>();

        for (Player player : players ) {
            if (player.getPoints() > maxPoints) {
                maxPoints = player.getPoints();
                winners.clear();
                winners.add(player);
            } else if (player.getPoints() == maxPoints) {
                winners.add(player);
            }
        }


        if (winners.size() > 1)
        {
            int maxObjectivePoints = 0;
            for (Player player : winners)
            {
                if (player.getObjectivePoints() > maxObjectivePoints)
                {
                    maxObjectivePoints = player.getObjectivePoints();
                    winnersAfterObjectivePointsCheck.clear();
                    winnersAfterObjectivePointsCheck.add(player);
                }
                else if (player.getObjectivePoints() == maxObjectivePoints)
                {
                    winnersAfterObjectivePointsCheck.add(player);
                }
            }
        }

        for (Player player : winnersAfterObjectivePointsCheck)
        {
            player.setWinner();
        }

        for (Player player : players)
        {
            if (player.getWinner() == 1)
            {
                //print hai vinto
            }
            else
            {
                //print hai perso
            }
        }

        //posso farlo con functional

    }


    /**
     * This method updates the attributes 'action' and 'playerOfTurn' preparing for
     * the next round, it updates also the roundsLeft attribute if it is set.
     * Moreover, it calls the 'checkEndGame' method to check if a player has reached 20 points, which
     * also sets the 'roundsLeft' attribute.
     */
    private void setupNextMove(){
        // ! to be implemented

        // if roundsLeft is not set check for endgame
        if(roundsLeft.isEmpty()) {
            checkEndGame();
        }else{
            // check if 0 rounds left and
            endGame(roundsLeft.get());
            return;
        }

        // updates stuff...
    }


    /**
     * This method checks weather player 'p' can perform action 'a'.
     * This method will be called to validate playGameCard, drawCardFromDeck, drawCardFromTable.
     * @param p is the player that wants to do something
     * @param a is the action that 'p' wants to perform
     * @return true is it is the expected action from the expected player.
     */
    private boolean validatesMove(Player p, ActionType a){
        // ! to be implemented

        return true;
    }
}
