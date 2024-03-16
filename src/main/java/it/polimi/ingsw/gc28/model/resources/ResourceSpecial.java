package it.polimi.ingsw.gc28.model.resources;

import it.polimi.ingsw.gc28.model.resources.utils.ResourceSpecialType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceType;
public class ResourceSpecial extends Resource {

    public ResourceSpecialType type;

    public ResourceSpecial(ResourceSpecialType type) {
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
        ResourceSpecial other = (ResourceSpecial) obj;
        return type == other.type;
    }
}
