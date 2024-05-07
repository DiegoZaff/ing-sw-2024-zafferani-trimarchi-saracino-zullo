package it.polimi.ingsw.gc28.model.challenge;

import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Table;
import it.polimi.ingsw.gc28.model.challenge.utils.ChallengeType;
import it.polimi.ingsw.gc28.model.resources.ResourceSpecial;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceSpecialType;

import java.io.Serializable;
import java.util.Optional;

public class ResourceChallenge extends Challenge implements Serializable {
    private final ResourceSpecial resourceChallenge;

    public ResourceChallenge(ChallengeType type, ResourceSpecial resourceSpecial){
        super(type);
        this.resourceChallenge = resourceSpecial;
    }

    @Override
    public int challengePoints(Table table, Coordinate coordinate){
        return table.getResourceCounters().get(this.resourceChallenge);
    }
}
