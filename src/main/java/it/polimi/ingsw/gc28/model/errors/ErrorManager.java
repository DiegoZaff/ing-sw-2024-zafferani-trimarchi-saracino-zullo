package it.polimi.ingsw.gc28.model.errors;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.actions.ActionManager;
import it.polimi.ingsw.gc28.model.actions.utils.ActionType;

import java.util.ArrayList;

public class ErrorManager {

    private ArrayList<Player> players;


    public ErrorManager(ArrayList<Player> players) {
        this.players = players;
    }


    public void fromWrongMove(Player player, ActionType action, ActionManager actionManager){


    }

    private void setErrorPlayer(Player player, PlayerActionError err){
        player.setError(err);
    }
}
