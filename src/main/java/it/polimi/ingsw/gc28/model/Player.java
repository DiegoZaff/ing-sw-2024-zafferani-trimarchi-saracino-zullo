package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.objectives.Objective;

import java.util.ArrayList;

public class Player {
    private int completedObjectives;

    private int points;


    private Objective objective;

    private ArrayList<CardGame> hand;

    private Table table;

    public ArrayList<CardGame> gethand (){
        return hand;
    }

    public int getPoints(){
        return points;
    }

    public void removeCard (CardGame cardToBeRemoved) {
        hand.remove(cardToBeRemoved);
    }

    public Table getTable () {return table;}

    /**
     * this method play a card in the table of a player, checking if the coordinate and the card can be played.
     * if the card is played increments points and modify table recources counters.
     * if the card is played it removes it from hand
     * it throws an IrregularCardException if the card can not be played.
     * it throws an IrregularCoordinateException if the coordinated can not be played.
     * @param playedCard the card to be played
     * @param isFront indicate how the card has to be played, front if True, back if False
     * @param coordinates indicates the coordinate where the card has to be played
     */
    public void PlayCard(CardGame playedCard, boolean isFront, Coordinate coordinates){
        if (!table.CheckPlayabilty(coordinates)){
            //potremmo creare una IrregularCordinateException, da aggiungere
        }else {
            if (isFront){
                playedCard.PlayFront(table, coordinates);
            } else {
                playedCard.PlayBack(table, coordinates);
            }
            //se la carta non viene giocata (carta oro non giocabile) possiamo aggiungere una IrregularCardException
            //da controllare se viene usato il metodo corretto

            //da implementare metodo per aggiornare contatori risorce

            //da implementare metodo per aumentare punteggio


            //rimuove la carta dalla mano
            removeCard(playedCard);
            
        }
    }


}
