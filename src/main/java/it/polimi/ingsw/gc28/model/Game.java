package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.cards.CardInitial;
import it.polimi.ingsw.gc28.model.objectives.Objective;

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

    private void playInitialCard (Player playingPlayer, CardInitial firstCard, boolean isFront, Coordinate coordinates /* 0,0 */) {


        /*

        *  firstcard viene aggiunta al tavolo alle coordinate 0,0
        *  coordinata 0,0 diventa ingiocabile
        *  coordinate ai vertici liberi diventano giocabili
        *  coordinate a vertici nulli diventano ingiocabili
        *  contatori di rosorsa aumentano di 1 per ogni risorsa dello stesso tipo sui vertici o al centro
        *
        *
        *
        *
        *
        *
        * */




     };

    private void playGameCard (Player playingPlayer, CardGame playedCard, boolean isFront, Coordinate coordinates ) {


        playingPlayer.removeCard(playingPlayer.hand, playedCard);
        /*
        * playingpPlayer non ha piu carta nella mano la carta
        * carta viene aggiunta al tavolo alle coordinate coordinates
        * coordinate del tavolo coordinates diventano ingiocabili
        * coordinate ai vertici liberi diventano giocabili a meno che non siano già ingiocabili
        * coordinate ai vertici nulli diventano ingiocabili
        * contatori delle risorse coperte dai vertici della carta decrementano di 1
        * contatori delle risorse presenti sulla carta auemtnano di 1 per ogni risorsa
        * verifica delle challenge se la carta è una cartaoro e calcolo dei punti
        * aumento del puntegigo se la carta da punti al piazzamento
        * */




     }

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
    };
}
