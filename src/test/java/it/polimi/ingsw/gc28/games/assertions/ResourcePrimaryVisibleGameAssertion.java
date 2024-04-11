package it.polimi.ingsw.gc28.games.assertions;

import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.resources.ResourcePrimary;
import it.polimi.ingsw.gc28.model.resources.utils.ResourcePrimaryType;

import java.util.Optional;

public class ResourcePrimaryVisibleGameAssertion extends GameAssertion{
    private final ResourcePrimaryType resourceType;
    private final int number;
    private int actualNumber;
    private final String nickmane;

    public ResourcePrimaryVisibleGameAssertion(ResourcePrimaryType resourceType, String name, int number) {
        this.resourceType = resourceType;
        this.number = number;
        this.nickmane = name;
    }

    @Override
    public boolean verifyAssertion(Game game) {
        Optional<Player> player =  super.getPlayerFromNick(nickmane, game);

        if(player.isEmpty()){
            return false;
        }
        actualNumber = player.get().getTable().getResourceCounters().get(new ResourcePrimary(resourceType));

        return actualNumber == number;
    }

    @Override
    public String toString() {
        return String.format("ResourceVisibleAssertion --- expectedNumber: %d, actual: %d", number, actualNumber);
    }
}
