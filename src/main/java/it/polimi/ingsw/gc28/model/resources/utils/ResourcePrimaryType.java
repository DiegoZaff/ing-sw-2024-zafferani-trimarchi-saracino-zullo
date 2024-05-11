package it.polimi.ingsw.gc28.model.resources.utils;

import it.polimi.ingsw.gc28.view.utils.Colors;

public enum ResourcePrimaryType {
    MUSHROOM,
    FOX,
    LEAF,
    BUTTERFLY;


    @Override
    public String toString() {
        return switch (this) {
            case MUSHROOM -> Colors.RED.getCode()+"Mu"+Colors.RESET.getCode();
            case FOX -> Colors.BLUE.getCode()+"Fo"+Colors.RESET.getCode();
            case LEAF -> Colors.GREEN.getCode()+"Le"+Colors.RESET.getCode();
            case BUTTERFLY -> Colors.PURPLE.getCode()+"Bu"+Colors.RESET.getCode();
        };
    }

    public String getResourceColor() {
        return switch (this) {
            case MUSHROOM -> Colors.RED.getCode();
            case FOX -> Colors.BLUE.getCode();
            case LEAF -> Colors.GREEN.getCode();
            case BUTTERFLY -> Colors.PURPLE.getCode();
        };
    }

}
