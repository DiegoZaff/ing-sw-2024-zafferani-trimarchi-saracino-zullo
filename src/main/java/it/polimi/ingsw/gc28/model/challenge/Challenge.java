package it.polimi.ingsw.gc28.model.challenge;

import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Table;
import it.polimi.ingsw.gc28.model.challenge.utils.ChallengeType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceSpecialType;

import java.util.Collection;
import java.util.Optional;

public class Challenge {

    public ChallengeType type;
    private final Optional<ResourceSpecialType> resourceChallenge;

    public Challenge(ChallengeType type, ResourceSpecialType resourceChallenge) {
        this.resourceChallenge = Optional.ofNullable(resourceChallenge);
        this.type=type;
    }

    public int challengePoints (Table table, Coordinate coordinate){
        return 0;
    }

}
