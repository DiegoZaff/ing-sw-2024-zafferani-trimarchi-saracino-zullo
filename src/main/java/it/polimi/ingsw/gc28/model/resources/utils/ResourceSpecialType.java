package it.polimi.ingsw.gc28.model.resources.utils;

import it.polimi.ingsw.gc28.view.utils.Colors;

public enum ResourceSpecialType {
    POTION,
    FEATHER,
    PARCHMENT,
    noResource;
    @Override
    public String toString() {
        String x = switch (this) {
            case POTION -> "Po";
            case FEATHER -> "Fe";
            case PARCHMENT -> "Pe";
            case noResource -> "  ";
        };
        return Colors.YELLOW.getCode()+x+Colors.RESET.getCode();
    }
}
