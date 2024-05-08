package it.polimi.ingsw.gc28.model.resources.utils;

public enum ResourcePrimaryType {
    MUSHROOM,
    FOX,
    LEAF,
    BUTTERFLY;


    @Override
    public String toString() {
        return switch (this) {
            case MUSHROOM -> "Mu";
            case FOX -> "Fo";
            case LEAF -> "Le";
            case BUTTERFLY -> "Bu";
        };
    }
}
