package it.polimi.ingsw.gc28.model.errors.types;

import it.polimi.ingsw.gc28.model.errors.PlayerActionError;

public class DeckHaveNoMoreCards extends PlayerActionError {
    public DeckHaveNoMoreCards(String deck) {
        super(deck + " have no more cards! You shouldn't be drawing!");
    }
}
