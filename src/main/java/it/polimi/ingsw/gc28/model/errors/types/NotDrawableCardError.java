package it.polimi.ingsw.gc28.model.errors.types;

import it.polimi.ingsw.gc28.model.errors.PlayerActionError;

public class NotDrawableCardError extends PlayerActionError {
    public NotDrawableCardError(String cardId) {
        super("Card " + cardId + " can't be drawn from visible cards!" );
    }
}
