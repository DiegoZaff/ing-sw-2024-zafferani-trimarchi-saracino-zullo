package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.actions.ActionManager;
import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.cards.CardObjective;
import it.polimi.ingsw.gc28.model.errors.ErrorManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class Game {

    private final ErrorManager errorManager;

    private final ActionManager actionManager;
    private ArrayList<CardObjective> globalObjectives;

    private final Deck deck;

    private final ArrayList<Player> players;

    /**
     * This attribute is null until a player reaches 20 points, counting
     * the number of rounds left to play. (it could be less if the deck finishes
     * the cards).
     */
    private Optional<Integer> roundsLeft;

    public Game(ArrayList<String> nicknames) throws IOException, IllegalArgumentException {
        if(nicknames == null || nicknames.size() < 2){
            throw new IllegalArgumentException();
        }
        this.deck = new Deck();
        this.players = nicknames.stream().map(Player::new)
                .collect(Collectors.toCollection(ArrayList::new));
        this.errorManager = new ErrorManager(this.players);
        this.actionManager = new ActionManager(this.players);
        this.roundsLeft = Optional.empty();

        // ! TODO: initialize globalObjectives
    }

    /**
     * This constructor is used only for testing purposes, because a know deck
     * is passed as a parameter.
     */
    public Game(ArrayList<String> nicknames, Deck deck){
        if(nicknames == null || nicknames.size() < 2){
            throw new IllegalArgumentException();
        }
        this.deck = deck;
        this.players = nicknames.stream().map(Player::new)
                .collect(Collectors.toCollection(ArrayList::new));
        this.errorManager = new ErrorManager(this.players);
        this.actionManager = new ActionManager(this.players);
        this.roundsLeft = Optional.empty();
    }


    /**
     * This method is responsible for checking if the player who has just
     * placed a card has reached 20 points and in that case it sets up a counter
     * which counts the rounds left.
     */
    private void checkEndGame() {

        Player playerOfTurn = actionManager.getPlayerOfTurn();

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
    private boolean endGame(int currRoundsLefts){
        if(currRoundsLefts == 0){
            calculateObjectivePoints();
            calculateWinner();
            // game ended
            return true;
        }else{
            roundsLeft = Optional.of(currRoundsLefts - 1);
            return false;
        }
    }


    /**
     * play a card in a player table
     * @param playingPlayer the person who is playing the card
     * @param playedCard the card to be played
     * @param isFront indicate how the card has to be played, front if True, back if False
     * @param coordinates indicates the coordinate where the card has to be played
     */
    public void playGameCard (Player playingPlayer, CardGame playedCard, boolean isFront, Coordinate coordinates ) {
        ActionType actionRequested = ActionType.PLAY_CARD;

        if(!actionManager.validatesMove(playingPlayer, actionRequested)){
            errorManager.fromWrongMove(playingPlayer, actionRequested, actionManager);
            return;
        };

        playingPlayer.playCard(playedCard, isFront, coordinates);

        // clean up all errors after a move is done successfully.
        errorManager.cleanUpAllErrors();

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
     * This method sets everything up for the next turn by calling checkEndGame() if
     * roundsLeft has not been set, or endGame() if roundsLeft has been set. At the end
     * it calls actionManager.nextMove() which updates the next turn's expected action and
     * playerOfTurn.
     */
    private void setupNextMove(){
        // if roundsLeft is not set check for endgame
        if(roundsLeft.isEmpty()) {
            checkEndGame();
        }else{
            // check if 0 rounds left and
            boolean hasEnded = endGame(roundsLeft.get());

            if(hasEnded){
                actionManager.gameFinished();
                return;
            }
        }

        // update for next move
        actionManager.nextMove();
    }

    /**
     * This method take the card from the top of the deck and add that card to the player's hand
     * @param playingPlayer
     * @param drawnCard
     */
    private void drawGameCard(Player playingPlayer, CardGame drawnCard){
    }
}
