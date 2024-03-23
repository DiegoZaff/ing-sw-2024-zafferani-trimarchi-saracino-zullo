package it.polimi.ingsw.gc28.model.cards;

import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Table;
import it.polimi.ingsw.gc28.model.challenge.utils.ChallengeType;
import it.polimi.ingsw.gc28.model.resources.Resource;
import it.polimi.ingsw.gc28.model.resources.ResourcePrimary;
import it.polimi.ingsw.gc28.model.resources.utils.ResourcePrimaryType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceSpecialType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class CardGold extends CardResource {
    private Map<ResourcePrimaryType, Integer> playability;
    private Optional<ChallengeType> challenge;
    private Optional<ResourceSpecialType> resourceChallenge;

    public CardGold(ResourceType[] resourceCard, ResourcePrimaryType resourcePrimary, int pointsPerPlay,
                    ResourcePrimaryType[] resourceNeeded, ChallengeType challenge, ResourceSpecialType resourceChallenge){
        super(resourceCard, resourcePrimary, pointsPerPlay);
        playability = new HashMap<>();
        createPlayabilityMap(playability, resourceNeeded);
        }

    public void createPlayabilityMap(Map<ResourcePrimaryType, Integer> playability, ResourcePrimaryType[] resourceNeeded){
        for(ResourcePrimaryType resource : resourceNeeded){
            if(playability.containsKey(resource)) {
                playability.put(resource, playability.get(resource) + 1);
            }
            else
            playability.put(resource, 1);
        }
    }

    @Override
    public void playFront(Table table, Coordinate playCoordinate){
        //come metodo super ma controllando se giocabile
    }
}


