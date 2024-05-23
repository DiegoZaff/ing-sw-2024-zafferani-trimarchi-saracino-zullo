package it.polimi.ingsw.gc28.model.cards;

import it.polimi.ingsw.gc28.model.objectives.Objective;
import it.polimi.ingsw.gc28.model.objectives.ObjectivePosition;
import it.polimi.ingsw.gc28.model.objectives.ObjectiveResources;
import it.polimi.ingsw.gc28.model.objectives.positions.Diagonal;
import it.polimi.ingsw.gc28.model.objectives.positions.PositionType;
import it.polimi.ingsw.gc28.model.objectives.positions.Stack;
import it.polimi.ingsw.gc28.model.objectives.positions.utils.DiagonalType;
import it.polimi.ingsw.gc28.model.objectives.positions.utils.GeneralPositionType;
import it.polimi.ingsw.gc28.model.objectives.positions.utils.PositionStackType;
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
    public CardObjective(String id,int points, ResourceType[] resourceNeeded) {
        super(id);
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
            } else if (resourceNeeded[i] == ResourceType.POTION){
                resourceCard[i] = new ResourceSpecial(ResourceSpecialType.POTION);
            }
        }
        HashMap<Resource, Integer> FinalResources = new HashMap<>();
        createMap(FinalResources, resourceCard);
        this.objective = new ObjectiveResources(FinalResources,points);
    }

    public void createMap(Map<Resource, Integer> TableResources, Resource[] Resources){
        for(Resource resource: Resources){
            if (resource != null){
                if(TableResources.containsKey(resource)) {
                    TableResources.put(resource, TableResources.get(resource) + 1);
                }
                else TableResources.put(resource, 1);
            }
        }
    }

    public CardObjective(String id,GeneralPositionType positionType, int points, ResourcePrimaryType[] resourcePosition){
        super(id);
        ResourcePrimary[] resourceCard = new ResourcePrimary[3];
        PositionType posType = null;

        for (int i=0; i<resourcePosition.length; i++) {
            if (resourcePosition[i] == ResourcePrimaryType.FOX) {
                resourceCard[i] = new ResourcePrimary(ResourcePrimaryType.FOX);
            } else if (resourcePosition[i] == ResourcePrimaryType.LEAF) {
                resourceCard[i] = new ResourcePrimary(ResourcePrimaryType.LEAF);
            } else if (resourcePosition[i] == ResourcePrimaryType.BUTTERFLY) {
                resourceCard[i] = new ResourcePrimary(ResourcePrimaryType.BUTTERFLY);
            } else if (resourcePosition[i] == ResourcePrimaryType.MUSHROOM) {
                resourceCard[i] = new ResourcePrimary(ResourcePrimaryType.MUSHROOM);
            } else {
                resourceCard[i] = null;
            }
        }

        if(positionType == GeneralPositionType.MAIN_DIAGONAL){
            posType = new Diagonal(DiagonalType.MAIN_DIAGONAL);
        } else if (positionType == GeneralPositionType.SECONDARY_DIAGONAL) {
            posType = new Diagonal(DiagonalType.SECONDARY_DIAGONAL);
        } else if (positionType == GeneralPositionType.N_E_STACK) {
            posType = new Stack(PositionStackType.N_E_STACK);
        } else if (positionType == GeneralPositionType.N_W_STACK) {
            posType = new Stack(PositionStackType.N_W_STACK);
        } else if (positionType == GeneralPositionType.S_E_STACK) {
            posType = new Stack(PositionStackType.S_E_STACK);
        } else if (positionType == GeneralPositionType.S_W_STACK) {
            posType = new Stack(PositionStackType.S_W_STACK);
        }
        this.objective = new ObjectivePosition(posType, points, resourceCard);
    }

    @Override
    public String toString(){
        return objective.toString();
    }


}
