package it.polimi.ingsw.gc28.games.assertions;

import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.cards.CardObjective;

import java.util.Optional;

public class ObjChosenAssertion extends GameAssertion{

    private final String playerNick;
    private final String cardObjId;

    private String actualChosenObjId;

    public ObjChosenAssertion(String playerNick, String cardObjId) {
        this.playerNick = playerNick;
        this.cardObjId = cardObjId;
        this.actualChosenObjId = null;
    }

    @Override
    public boolean verifyAssertion(Game game) {

        Optional<Player> player = super.getPlayerFromNick(playerNick, game);

        if(player.isEmpty()){
            return false;
        }

        Optional<CardObjective> chosenObj = player.get().getObjectiveChosen();

        if(chosenObj.isEmpty()){
            return false;
        }else{
            actualChosenObjId = chosenObj.get().getId();
        }

        return actualChosenObjId.equals(cardObjId);

    }

    @Override
    public String toString() {
        return String.format("ObjChosenAssertion --- expectedPoints: %s, actual: %s", cardObjId, actualChosenObjId);
    }
}
