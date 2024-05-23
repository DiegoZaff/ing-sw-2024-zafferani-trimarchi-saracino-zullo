package it.polimi.ingsw.gc28.model.utils;

public enum PlayerColor {
    RED,
    BLUE,
    GREEN,
    YELLOW;

    public static PlayerColor customValueOf(String value) {
        for (PlayerColor color : values()) {
            if (color.name().equalsIgnoreCase(value)) {
                return color;
            }
        }
        throw new IllegalArgumentException("No enum constant with text " + value + " found");
    }
}
