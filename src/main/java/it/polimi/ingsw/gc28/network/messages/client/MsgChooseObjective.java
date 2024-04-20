package it.polimi.ingsw.gc28.network.messages.client;

import it.polimi.ingsw.gc28.controller.GameController;
import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;

public class MsgChooseObjective extends MessageC2S {
    String playerName;
    String cardId;
    public MsgChooseObjective(String playerName, String gameId, String cardId) {
        super(gameId);
        this.playerName = playerName;
        this.cardId = cardId;
    }

    @Override
    public void execute(GameController controller) {
        controller.chooseObjectivePersonal(playerName, cardId);
    }
}
