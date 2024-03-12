package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.cards.CardResource;
import it.polimi.ingsw.gc28.model.objectives.Objective;

import java.util.ArrayList;

public class Player {
    private int completedObjectives;

    private int points;

    private Objective objective;

    private ArrayList<CardResource> hand;
}
