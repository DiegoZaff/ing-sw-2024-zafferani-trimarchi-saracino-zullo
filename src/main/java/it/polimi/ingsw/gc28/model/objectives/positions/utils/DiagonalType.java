package it.polimi.ingsw.gc28.model.objectives.positions.utils;

import it.polimi.ingsw.gc28.view.utils.Colors;

public enum DiagonalType {
    MAIN_DIAGONAL,
    SECONDARY_DIAGONAL;


    public String toString(String color1, String color2, String color3) {
        return switch (this){
            case MAIN_DIAGONAL -> String.format("""
                __________________
                |         %s[ ]%s    |
                |      %s[ ]%s       |
                |   %s[ ]%s          |
                ------------------
                """,color3, Colors.RESET.getCode(), color2, Colors.RESET.getCode(),color1, Colors.RESET.getCode());
            case SECONDARY_DIAGONAL -> String.format("""
                __________________
                |   %s[ ]%s          |
                |      %s[ ]%s       |
                |         %s[ ]%s    |
                ------------------
                """,color1, Colors.RESET.getCode(), color2, Colors.RESET.getCode(),color3, Colors.RESET.getCode());
        };
    }
}
