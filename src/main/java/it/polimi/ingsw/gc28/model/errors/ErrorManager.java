package it.polimi.ingsw.gc28.model.errors;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.actions.ActionManager;
import it.polimi.ingsw.gc28.model.actions.utils.ActionType;

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
        if(player != expectedPlayer){
            player.setError(Optional.of(new NotYourTurnError()));
        }else if(expectedAction != action){
            player.setError(Optional.of(new UnexpectedMoveError()));
        }

    }

    private void setErrorPlayer(Player player, PlayerActionError err){
        player.setError(Optional.of(err));
    }

    public void cleanUpAllErrors(){
        for (Player p : players){
            p.setError(Optional.empty());
        }
    }
}