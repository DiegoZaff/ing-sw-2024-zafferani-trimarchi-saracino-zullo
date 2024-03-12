package it.polimi.ingsw.gc28.model.resources;

public class ResourcePrimary extends Resource {

    enum ResourcePrimaryType {
        MUSHROOM,
        FOX,
        LEAF,
        BUTTERFLY
    }
    private final ResourcePrimaryType type;

    public ResourcePrimary(ResourcePrimaryType type) {
        this.type = type;
    }



}


