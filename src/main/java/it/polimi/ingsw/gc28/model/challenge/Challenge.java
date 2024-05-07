package it.polimi.ingsw.gc28.model.challenge;

import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Table;
import it.polimi.ingsw.gc28.model.challenge.utils.ChallengeType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceSpecialType;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

public abstract class Challenge  implements Serializable {
    public ChallengeType type;
    //per ora lo lascio, ma si può eliminare

    public Challenge(ChallengeType type) {
        this.type=type;
    }

    public abstract int challengePoints (Table table, Coordinate coordinate);

}
