package it.polimi.ingsw.gc28.model.resources;

import it.polimi.ingsw.gc28.model.resources.utils.ResourcePrimaryType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceType;

public class ResourcePrimary extends Resource {
    public ResourcePrimaryType type;

    public ResourcePrimary(ResourcePrimaryType type) {
        this.type = type;
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

}


