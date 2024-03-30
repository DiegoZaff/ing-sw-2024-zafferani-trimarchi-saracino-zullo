package it.polimi.ingsw.gc28.model.cards;

import it.polimi.ingsw.gc28.model.objectives.Objective;
import it.polimi.ingsw.gc28.model.objectives.ObjectiveResources;
import it.polimi.ingsw.gc28.model.resources.Resource;
import it.polimi.ingsw.gc28.model.resources.ResourcePrimary;
import it.polimi.ingsw.gc28.model.resources.ResourceSpecial;
import it.polimi.ingsw.gc28.model.resources.utils.ResourcePrimaryType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceSpecialType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceType;

import java.util.HashMap;
import java.util.Map;

public class CardObjective extends Card {
    private final Objective objective;
    public Objective getObjective(){
        return objective;
    }
    public CardObjective(int points, ResourceType[] resourceNeeded) {
        Objective objective1;
        Resource[] resourceCard = new Resource[3];

        for (int i=0; i<resourceNeeded.length; i++) {
            if (resourceNeeded[i] == ResourceType.FOX) {
                resourceCard[i] = new ResourcePrimary(ResourcePrimaryType.FOX);
            } else if (resourceNeeded[i] == ResourceType.LEAF) {
                resourceCard[i] = new ResourcePrimary(ResourcePrimaryType.LEAF);
            } else if (resourceNeeded[i] == ResourceType.BUTTERFLY) {
                resourceCard[i] = new ResourcePrimary(ResourcePrimaryType.BUTTERFLY);
            } else if (resourceNeeded[i] == ResourceType.MUSHROOM) {
                resourceCard[i] = new ResourcePrimary(ResourcePrimaryType.MUSHROOM);
            } else if (resourceNeeded[i] == ResourceType.FEATHER) {
                resourceCard[i] = new ResourceSpecial(ResourceSpecialType.FEATHER);
            } else if (resourceNeeded[i] == ResourceType.PARCHMENT) {
                resourceCard[i] = new ResourceSpecial(ResourceSpecialType.PARCHMENT);
            } else if (resourceNeeded[i] == ResourceType.POTION) {
                resourceCard[i] = new ResourceSpecial(ResourceSpecialType.POTION);
            } else {
                resourceCard[i] = null;
            }
        }
        HashMap<Resource, Integer> FinalResources = new HashMap<>();
        createMap(FinalResources, resourceCard);
        objective1 = new ObjectiveResources(FinalResources,points);
        this.objective = objective1;
    }

    public void createMap(Map<Resource, Integer> TableResources, Resource[] Resources){
        for(Resource resource: Resources){
            if(TableResources.containsKey(resource)) {
                TableResources.put(resource, TableResources.get(resource) + 1);
            }
            else TableResources.put(resource, 1);
        }
    }

    public CardObjective(int points, ResourcePrimaryType[] resourceN){

    }
}
