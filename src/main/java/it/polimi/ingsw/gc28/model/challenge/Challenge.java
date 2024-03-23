package it.polimi.ingsw.gc28.model.challenge;

import it.polimi.ingsw.gc28.model.challenge.utils.ChallengeType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceSpecialType;

import java.util.Optional;

public class Challenge {

    public ChallengeType type;
    private final Optional<ResourceSpecialType> resourceChallenge;

    public Challenge(ResourceSpecialType resourceChallenge) {
        this.resourceChallenge = Optional.ofNullable(resourceChallenge);
    }
}
