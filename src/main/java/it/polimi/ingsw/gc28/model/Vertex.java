package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.resources.Resource;

public class Vertex {
    private final Resource resource;

    public Vertex(Resource resource){
        this.resource= resource;
    }
    public Resource getResource() {
        return resource;
    }
}
