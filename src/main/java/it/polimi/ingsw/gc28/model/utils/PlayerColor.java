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

    public String getHexCodeLight() {
        switch (this) {
            case RED:
                return "#FFE6E6";
            case BLUE:
                return "#C2FFED";
            case GREEN:
                return "#C2FFC8";
            case YELLOW:
                return "#F7FFC2";
            default:
                throw new IllegalArgumentException("No hex code for color: " + this);
        }
    }

    public String getHexCodeDark() {
        switch (this) {
            case RED:
                return "#FF9D9D";
            case BLUE:
                return "#79FFD7";
            case GREEN:
                return "#98DA9E";
            case YELLOW:
                return "#CDD793";
            default:
                throw new IllegalArgumentException("No hex code for color: " + this);
        }
    }

}
