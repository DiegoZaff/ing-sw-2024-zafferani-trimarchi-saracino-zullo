package it.polimi.ingsw.gc28.model.resources.utils;

public enum ResourceSpecialType {
    POTION,
    FEATHER,
    PARCHMENT,
    noResource;
    @Override
    public String toString() {
        return switch (this) {
            case POTION -> "Po";
            case FEATHER -> "Fe";
            case PARCHMENT -> "Pe";
            case noResource -> "  ";
        };
    }
}
