package it.polimi.ingsw.gc28.model.errors.types;
import it.polimi.ingsw.gc28.model.errors.PlayerActionError;

public class NotPlayableGoldCard extends PlayerActionError {
    public NotPlayableGoldCard (String cardId){
        super("Gold card "+ cardId +" can not be played, you need more resources");
    }
}
