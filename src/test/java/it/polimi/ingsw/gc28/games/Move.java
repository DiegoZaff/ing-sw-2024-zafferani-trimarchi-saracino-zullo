package it.polimi.ingsw.gc28.games;

import it.polimi.ingsw.gc28.model.actions.utils.ActionType;

public class Move {
    private String player;
    private ActionType action;
    private boolean isFront;

    public Move(String player, ActionType action, boolean isFront) {
        this.player = player;
        this.action = action;
        this.isFront = isFront;
    }

    public String getPlayer() {
        return player;
    }


    public ActionType getAction() {
        return action;
    }


    public boolean isFront() {
        return isFront;
    }

    @Override
    public String toString() {
        return "Move{" +
                "player='" + player + '\'' +
                ", action=" + action +
                ", isFront=" + isFront +
                '}';
    }
}
