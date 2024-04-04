package it.polimi.ingsw.gc28.model.errors;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.actions.ActionManager;
import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.model.errors.types.*;

import java.util.ArrayList;
import java.util.Optional;

public class ErrorManager {

    private final ArrayList<Player> players;


    public ErrorManager(ArrayList<Player> players) {
        this.players = players;
    }


    /**
     * Manages errors resulting from playing the wrong move or being the wrong player to move.
     */
    public void fromWrongMove(Player player, ActionType action, ActionManager actionManager){
        Player expectedPlayer = actionManager.getPlayerOfTurn();
        ActionType expectedAction = actionManager.getActionType();

        // one or the other.
        if(expectedAction.equals(ActionType.CHOOSE_OBJ)){
            if(!action.equals(ActionType.CHOOSE_OBJ)){
                player.setError(new NotYourTurnError());
            }else if(player.getObjectiveChosen().isPresent()){
                player.setError(new AlreadyChoseObjectiveError());
            }
        }
        if(player != expectedPlayer){
            player.setError(new NotYourTurnError());
        }else if(expectedAction != action){
            player.setError(new UnexpectedMoveError());
        }

    }

    public void fromInvalidObjectiveChoice(Player player){
        player.setError(new InvalidObjectiveChoiceError());
    }

    private void setErrorPlayer(Player player, PlayerActionError err){
        player.setError(err);
    }

    public void cleanUpAllErrors(){
        for (Player p : players){
            p.setError(null);
        }
    }
}
