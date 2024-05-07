package it.polimi.ingsw.gc28.View;

import it.polimi.ingsw.gc28.model.Table;
import it.polimi.ingsw.gc28.model.cards.CardInitial;
import it.polimi.ingsw.gc28.model.cards.CardObjective;
import it.polimi.ingsw.gc28.model.cards.CardResource;

import java.io.Serializable;
import java.util.ArrayList;

public class PrivateRepresentation implements Serializable {

    private ArrayList<CardResource> hand;

    private CardObjective privateObjective;

    private Table table;

    private CardInitial cardInitial;

    public PrivateRepresentation (CardObjective objective, Table table, ArrayList<CardResource> hand,  CardInitial cardInitial){
        this.cardInitial = cardInitial;
        this.hand = hand;
        this.table = table;
        this.privateObjective = objective;

    }

    public ArrayList<CardResource> getHand() {
        return hand;
    }

    public CardObjective getPrivateObjective() {
        return privateObjective;
    }

    public Table getTable() {
        return table;
    }

    public CardInitial getCardInitial() {
        return cardInitial;
    }
}
