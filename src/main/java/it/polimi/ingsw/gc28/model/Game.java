package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.cards.CardInitial;
import it.polimi.ingsw.gc28.model.objectives.Objective;
import it.polimi.ingsw.gc28.model.resources.Resource;

import java.util.ArrayList;
import java.util.Optional;

public class Game {

    enum ActionType{
        PLAY_INITIAL_CARD,
        PLAY_CARD,
        DRAW_CARD,
        CHOOSE_OBJ,
    }

    private ActionType action;

    private Player playerOfTurn;

    private ArrayList<Objective> globalObjectives;

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

    private void playInitialCard (Player playingPlayer, CardInitial firstCard, boolean isFront, Coordinate coordinates /* 0,0 */) {
        /*
         *  firstcard viene aggiunta al tavolo alle coordinate 0,0
         *  coordinata 0,0 diventa ingiocabile
         *  coordinate ai vertici liberi diventano giocabili
         *  coordinate a vertici nulli diventano ingiocabili
         *  contatori di rosorsa aumentano di 1 per ogni risorsa dello stesso tipo sui vertici o al centro
         *
         */
    }

    private void playGameCard (Player playingPlayer, CardGame playedCard, boolean isFront, Coordinate coordinates ) {
        //illegal coordinates exception?

        playingPlayer.removeCard(playedCard);  // Removes card from hand

        playingPlayer.getTable().addUnplayableCoordinate(coordinates); // Adds coordinates to unplayablecoordinates

        playingPlayer.getTable().removePlayableCoordinate(coordinates); // removes coordinates from playable coordinates


        /*
        * guardo la carta e in base al contenuto di ogni vertice aggiugo se ho cooridnate che diventano ingiocabili
        * le coordinate possono essere duplicate ma al massimo ci osno 4 volte per ogni coordinata, si possono inserire controlli ma rallenta tanto
        * anche èerche non avro spesso bisongo di ocntrollare le unplayable
        *
        * il seguente è un l'updare delle arraylist di unplayableCoordinates e playablecoordinates in base alla carta piazzata
        * */

        Resource resourceToBeChecked = playedCard.getResourceInsideVertex(0);  //salvo che risorsa è
        Coordinate coordinateToBeChecked = new Coordinate( coordinates.getX() -1, coordinates.getY() +1); //salvo la coordinata da aggiunger eo rimuovere
        if (resourceToBeChecked == null){ //se ho un vertice null
            playingPlayer.getTable().addUnplayableCoordinate(coordinateToBeChecked);
            playingPlayer.getTable().removePlayableCoordinate(coordinateToBeChecked);
        }
        else //se ho un vertice non null
        {
            if (!playingPlayer.getTable().alreadyUnplayable(coordinateToBeChecked)) //se non è presente in unpplayable
            {
                playingPlayer.getTable().addPlayableCoordinate(coordinateToBeChecked); //aggiungo alle playable
            }
        }

        resourceToBeChecked = playedCard.getResourceInsideVertex(1);
        coordinateToBeChecked = new Coordinate(coordinates.getX() +1, coordinates.getY() +1);
        if (resourceToBeChecked == null){
            playingPlayer.getTable().addUnplayableCoordinate(coordinateToBeChecked);
            playingPlayer.getTable().removePlayableCoordinate(coordinateToBeChecked);

        }
        else
        {
            if (!playingPlayer.getTable().alreadyUnplayable(coordinateToBeChecked))
            {
                playingPlayer.getTable().addPlayableCoordinate(coordinateToBeChecked);
            }
        }

        resourceToBeChecked = playedCard.getResourceInsideVertex(2);
        coordinateToBeChecked = new Coordinate(coordinates.getX() +1, coordinates.getY() -1);

        if (resourceToBeChecked == null){
            playingPlayer.getTable().addUnplayableCoordinate(coordinateToBeChecked);
            playingPlayer.getTable().removePlayableCoordinate(coordinateToBeChecked);

        }
        else
        {
            if (!playingPlayer.getTable().alreadyUnplayable(coordinateToBeChecked))
            {
                playingPlayer.getTable().addPlayableCoordinate(coordinateToBeChecked);
            }
        }


        resourceToBeChecked = playedCard.getResourceInsideVertex(3);
        coordinateToBeChecked = new Coordinate(coordinates.getX() -1, coordinates.getY() -1);
        if (resourceToBeChecked == null){
            playingPlayer.getTable().addUnplayableCoordinate(coordinateToBeChecked);
            playingPlayer.getTable().removePlayableCoordinate(coordinateToBeChecked);

        }
        else
        {
            if (!playingPlayer.getTable().alreadyUnplayable(coordinateToBeChecked))
            {
                playingPlayer.getTable().addPlayableCoordinate(coordinateToBeChecked);
            }
        }
        //fine dell'update delle coordinate playable e unplayable


        /*
         * playingpPlayer non ha piu carta nella mano la carta OK
         * carta viene aggiunta al tavolo alle coordinate coordinates
         * coordinate del tavolo coordinates diventano ingiocabili OK
         * coordinate ai vertici liberi diventano giocabili a meno che non siano già ingiocabili OK
         * coordinate ai vertici nulli diventano ingiocabili OK
         * contatori delle risorse coperte dai vertici della carta decrementano di 1
         * contatori delle risorse presenti sulla carta auemtnano di 1 per ogni risorsa
         * verifica delle challenge se la carta è una cartaoro e calcolo dei punti
         * aumento del puntegigo se la carta da punti al piazzamento
         * */




    }
}
