package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.cards.CardResource;
import it.polimi.ingsw.gc28.model.objectives.Objective;

import java.util.ArrayList;

public class Player {
    private int completedObjectives;

    private int points;

    /*
    *  rimuove la carta passata come parametro dalla mano
    *
    *
    * */
    public void removeCard (ArrayList<CardResource> hand, CardGame cardToBeRemoved) {
        for (CardResource card : hand) {
            if (card == cardToBeRemoved) // le carte devono essere cnonfrontate, avranno un codice identificativo? equals? ==?
            {
                hand.remove(card);
            }

        }

    }


    public int getPoints(){
        return points;
    }

    private Objective objective;

    public ArrayList<CardResource> hand; //reso pubblico per far funzionare removecard non so se si può

    private Table table;
}
