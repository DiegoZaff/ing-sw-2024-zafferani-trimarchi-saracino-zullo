package it.polimi.ingsw.gc28.model.objectives.positions.utils;

import it.polimi.ingsw.gc28.view.utils.Colors;

public enum PositionStackType{
    N_E_STACK,
    N_W_STACK,
    S_E_STACK,
    S_W_STACK;

    public String toString(String color1, String color2, String color3){
        return switch (this){
            case N_E_STACK -> String.format("""
                __________________
                |         %s[ ]%s    |
                |      %s[ ]%s       |
                |      %s[ ]%s       |
                ------------------
                """,color3, Colors.RESET.getCode(), color2, Colors.RESET.getCode(),color1, Colors.RESET.getCode());

            case S_E_STACK -> String.format("""
                __________________
                |      %s[ ]%s       |
                |      %s[ ]%s       |
                |         %s[ ]%s    |
                ------------------
                """,color2, Colors.RESET.getCode(), color1, Colors.RESET.getCode(),color3, Colors.RESET.getCode());

            case S_W_STACK -> String.format("""
                __________________
                |      %s[ ]%s       |
                |      %s[ ]%s       |
                |   %s[ ]%s          |
                ------------------
                """,color1, Colors.RESET.getCode(), color2, Colors.RESET.getCode(),color3, Colors.RESET.getCode());

            case N_W_STACK -> String.format("""
                __________________
                |   %s[ ]%s          |
                |      %s[ ]%s       |
                |      %s[ ]%s       |
                ------------------
                """,color1, Colors.RESET.getCode(), color3, Colors.RESET.getCode(),color2, Colors.RESET.getCode());
        };
    }
}
