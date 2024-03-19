package it.polimi.ingsw.gc28.model.cards;

import it.polimi.ingsw.gc28.model.objectives.Objective;

public class CardObjective extends Card {

    private final Objective objective;


    public Objective getObjective(){
        return objective;
    }

    public CardObjective(Objective objective) {
        this.objective = objective;
    }

}
