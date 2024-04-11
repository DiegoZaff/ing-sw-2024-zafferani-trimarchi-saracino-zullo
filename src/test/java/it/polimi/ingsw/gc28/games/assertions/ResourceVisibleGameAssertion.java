package it.polimi.ingsw.gc28.games.assertions;

import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.model.Player;

import java.util.Optional;

public class ResourceVisibleGameAssertion extends GameAssertion{
    private final String resourceType;
    private final int number;
    private int actualNumber;
    private final String nickmane;

    public ResourceVisibleGameAssertion(String resourceType, String name, int number) {
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
        actualNumber = player.get().getTable().getResourceCounters().get(resourceType);

        return actualNumber == number;
    }

    @Override
    public String toString() {
        return String.format("ResourceVisibleAssertion --- expectedNumber: %d, actual: %d", number, actualNumber);
    }
}
