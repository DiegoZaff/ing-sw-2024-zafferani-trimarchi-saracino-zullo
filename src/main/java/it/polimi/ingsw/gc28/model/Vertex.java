package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.resources.Resource;

import java.util.Optional;

public class Vertex {
    private final Optional<Resource> resource;

    public boolean isExists() {
        return exists;
    }

    private final boolean exists;


    public Vertex(boolean exists){
        this.resource = Optional.empty();
        this.exists = exists;
    }

    public Vertex(Resource res){
        this.resource = Optional.of(res);
        this.exists = true;
    }


    public Optional<Resource> getResource() {
        return resource;
    }
}
