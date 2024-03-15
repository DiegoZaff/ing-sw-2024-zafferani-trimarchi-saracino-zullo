package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.cards.CardResource;
import it.polimi.ingsw.gc28.model.objectives.Objective;

import java.util.ArrayList;

public class Player {
    private int completedObjectives;

    private int points;


    private Objective objective;

    private ArrayList<CardResource> hand;

    private Table table;

    public ArrayList<CardResource> gethand (){
        return hand;
    }

    public int getPoints(){
        return points;
    }

    public void removeCard (CardGame cardToBeRemoved) {
        for (CardResource card : hand) {
            if (card == cardToBeRemoved) // le carte devono essere confrontate, avranno un codice identificativo? equals? ==?
            {
                hand.remove(card);
            }

        }

    }

    public Table getTable () {return table;}


}
