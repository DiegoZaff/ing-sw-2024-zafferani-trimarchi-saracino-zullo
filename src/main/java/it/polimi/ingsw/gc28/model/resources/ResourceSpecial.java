package it.polimi.ingsw.gc28.model.resources;

public class ResourceSpecial extends Resource {
    enum ResourceSpecialType {
        POTION,
        FEATHER,
        PARCHMENT
    }

    private final ResourceSpecialType type;

    public ResourceSpecial(ResourceSpecialType type) {
        this.type = type;
    }
}
