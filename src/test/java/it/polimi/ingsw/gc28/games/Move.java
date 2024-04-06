package it.polimi.ingsw.gc28.games;

import it.polimi.ingsw.gc28.games.assertions.GameAssertion;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.cards.CardObjective;

import java.util.ArrayList;
import java.util.Optional;

public class Move {
    private final String player;
    private final ActionType action;
    private final Boolean isFront;

    private final CardGame card;

    private final CardObjective cardObjective;


    private final Coordinate coord;

    private final ArrayList<GameAssertion> assertions;

    public Move(String player, ActionType action, Boolean isFront, CardGame card, CardObjective cardObjective, Coordinate coord, ArrayList<GameAssertion> assertions) {
        this.player = player;
        this.action = action;
        this.isFront = isFront;
        this.card = card;
        this.cardObjective = cardObjective;
        this.coord = coord;
        this.assertions = assertions;
    }

    public String getPlayer() {
        return player;
    }


    public ActionType getAction() {
        return action;
    }


    public Optional<Boolean> isFront() {
        return Optional.ofNullable(isFront);
    }


    @Override
    public String toString() {
        return "Move{" +
                "player='" + player + '\'' +
                ", action=" + action +
                ", isFront=" + isFront +
                '}';
    }

    public Optional<CardGame> getCard() {
        return Optional.ofNullable(card);
    }

    public Optional<CardObjective> getCardObjective() {
        return Optional.ofNullable(cardObjective);
    }

    public Optional<Coordinate> getCoord() {
        return Optional.ofNullable(coord);
    }

    public ArrayList<GameAssertion> getAssertions() {
        return assertions;
    }
}
