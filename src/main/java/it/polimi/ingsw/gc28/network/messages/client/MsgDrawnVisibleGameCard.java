package it.polimi.ingsw.gc28.network.messages.client;

import it.polimi.ingsw.gc28.controller.GameController;

public class MsgDrawnVisibleGameCard extends MessageC2S{
    String playerName;
    String cardId;
    protected MsgDrawnVisibleGameCard(String playerName, String gameId, String cardId) {
        super(gameId);
        this.playerName = playerName;
        this.cardId = cardId;
    }

    @Override
    public void execute(GameController controller) {
        controller.drawCard(playerName, cardId);
    }
}
