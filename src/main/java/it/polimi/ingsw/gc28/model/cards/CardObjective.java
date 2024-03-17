package it.polimi.ingsw.gc28.model.cards;

import it.polimi.ingsw.gc28.model.objectives.Objective;

public class CardObjective extends Card {

    private final Objective objective;

    private final int points;

    public Objective getObjective(){
        return objective;
    }

    public CardObjective(Objective objective, int points) {
        this.objective = objective;
        this.points = points;
    }

}
