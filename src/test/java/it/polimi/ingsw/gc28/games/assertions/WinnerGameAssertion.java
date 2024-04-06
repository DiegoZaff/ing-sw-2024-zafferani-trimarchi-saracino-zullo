package it.polimi.ingsw.gc28.games.assertions;

import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.model.Player;

import java.util.ArrayList;

public class WinnerGameAssertion extends GameAssertion{

    private final ArrayList<String> nicks;

    public WinnerGameAssertion(ArrayList<String> nicks) {
        this.nicks = nicks;
    }

    @Override
    public boolean verifyAssertion(Game game) {
        ArrayList<Player> winners = game.getWinners();

        if(nicks.size() != winners.size()){
            return false;
        }

        // check that all nicks correspond to winners.
        for (int i = 0; i < winners.size(); i++){
            if(!winners.get(i).getName().equals(nicks.get(i))){
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return "WinnerPlayers Assertion";
    }
}
