package it.polimi.ingsw.gc28.view.utils;

import it.polimi.ingsw.gc28.model.utils.PlayerColor;

public class PlayerColorInfo {


    private final String name;

    private final PlayerColor color;

    public PlayerColorInfo(String name, PlayerColor color) {
        this.name = name;
        this.color = color;
    }


    public String getName() {
        return name;
    }

    public PlayerColor getColor() {
        return color;
    }
}
