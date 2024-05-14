package it.polimi.ingsw.gc28.model.errors.types;

import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.errors.PlayerActionError;

public class NotOwnedCard extends PlayerActionError {
    public NotOwnedCard(CardGame card){
        super("you don't have the card: "+card.toString());
    }

}
