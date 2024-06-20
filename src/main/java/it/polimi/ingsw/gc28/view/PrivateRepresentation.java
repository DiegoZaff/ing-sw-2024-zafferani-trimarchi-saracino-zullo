package it.polimi.ingsw.gc28.view;

import it.polimi.ingsw.gc28.model.Table;
import it.polimi.ingsw.gc28.model.cards.CardInitial;
import it.polimi.ingsw.gc28.model.cards.CardObjective;
import it.polimi.ingsw.gc28.model.cards.CardResource;
import it.polimi.ingsw.gc28.model.utils.PlayerColor;

import java.io.Serializable;
import java.util.ArrayList;

public class PrivateRepresentation implements Serializable {

    private ArrayList<CardResource> hand;

    private CardObjective privateObjective;

    private Table table;

    private CardInitial cardInitial;

    private ArrayList<CardObjective> objsToChoose;

    private PlayerColor color;

    private boolean isWinner;

    public PrivateRepresentation (CardObjective objective, Table table, ArrayList<CardResource> hand, CardInitial cardInitial, ArrayList<CardObjective> objsToChoose, PlayerColor color, boolean isWinner){
        this.cardInitial = cardInitial;
        this.hand = hand;
        this.table = table;
        this.privateObjective = objective;
        this.objsToChoose = objsToChoose;
        this.color = color;
        this.isWinner = isWinner;
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

    public ArrayList<CardObjective> getObjsToChoose() {
        return objsToChoose;
    }

    public PlayerColor getColor() {
        return color;
    }

    public boolean isWinner() {
        return isWinner;
    }
}
