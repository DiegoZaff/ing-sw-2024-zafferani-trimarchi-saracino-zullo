package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.resources.Resource;

import java.io.Serializable;
import java.util.Optional;

/**
 * this class is the corner of a card, it can be empty,
 * it can contain a resource, or it can be void when a card can not be played on that vertex
 */
public class Vertex implements Serializable {
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
