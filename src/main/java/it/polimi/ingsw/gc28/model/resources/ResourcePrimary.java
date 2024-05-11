package it.polimi.ingsw.gc28.model.resources;

import it.polimi.ingsw.gc28.model.resources.utils.ResourcePrimaryType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceType;

import java.util.Objects;

public class ResourcePrimary extends Resource {
    public ResourcePrimaryType type;

    public ResourcePrimary(ResourcePrimaryType type) {
        this.type = type;
    }

    public ResourcePrimaryType getType(){
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ResourcePrimary other = (ResourcePrimary) obj;
        return type == other.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public String toString(){
        return type.toString();
    }
}


