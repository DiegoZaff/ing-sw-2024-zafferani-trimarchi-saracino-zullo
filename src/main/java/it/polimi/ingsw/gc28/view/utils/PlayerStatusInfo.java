package it.polimi.ingsw.gc28.view.utils;

import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.model.utils.PlayerColor;

public class PlayerStatusInfo {
    private final String name;

    private final PlayerColor color;

    private final int points;

    private final boolean showTrophy;

    private final Integer roundsLeft;

    private final ActionType action;

    private final String playerOfTurn;


    public PlayerStatusInfo(String name, PlayerColor color, int points, boolean showTrophy, Integer roundsLeft, ActionType action, String playerOfTurn) {
        this.name = name;
        this.color = color;
        this.points = points;
        this.showTrophy = showTrophy;
        this.roundsLeft = roundsLeft;
        this.action = action;
        this.playerOfTurn = playerOfTurn;
    }

    public boolean isShowTrophy() {
        return showTrophy;
    }

    public int getPoints() {
        return points;
    }

    public PlayerColor getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public Integer getRoundsLeft() {
        return roundsLeft;
    }

    public ActionType getAction() {
        return action;
    }

    public String getPlayerOfTurn() {
        return playerOfTurn;
    }
}
