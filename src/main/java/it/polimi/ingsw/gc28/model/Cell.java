package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.cards.Card;
import it.polimi.ingsw.gc28.model.objectives.Objective;

import java.util.ArrayList;

public class Cell {

    private final Card card;

    private ArrayList<Objective> objectivesSatisfied;

    private final int orderPlay;

    private final boolean isPlayedFront;


    public Cell(Card card, int orderPlay, boolean isPlayedFront) {
        this.card = card;
        this.orderPlay = orderPlay;
        this.isPlayedFront = isPlayedFront;
    }
}
