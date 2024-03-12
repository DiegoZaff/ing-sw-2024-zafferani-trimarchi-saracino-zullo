package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.objectives.Objective;

import java.util.ArrayList;

public class Game {

    enum ActionType{
        PLAY_INITIAL_CARD,
        PLAY_CARD,
        DRAW_CARD,
        CHOOSE_OBJ,
    }

    private ActionType action;

    private Player turn;

    private ArrayList<Objective> globalObjectives;

    private Deck deck;

    private ArrayList<Player> players;


}
