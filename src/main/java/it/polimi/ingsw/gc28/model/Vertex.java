package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.resources.Resource;

import java.util.Optional;

public class Vertex {
    private final Resource resource;
    private final boolean exists;

    public boolean isExists() {
        return exists;
    }

    public Vertex(boolean exists){
        this.resource = null;
        this.exists = exists;
    }

    public Vertex(Resource res){
        this.resource = res;
        this.exists = true;
    }


    public Optional<Resource> getResource() {
        return Optional.ofNullable(resource);
    }
}
