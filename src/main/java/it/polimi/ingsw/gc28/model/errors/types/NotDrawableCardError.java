package it.polimi.ingsw.gc28.model.errors.types;

public class NotDrawableCardError extends PlayerActionError{
    public NotDrawableCardError(String cardId) {
        super("Card " + cardId + " can't be drawn from visible cards!" );
    }
}
