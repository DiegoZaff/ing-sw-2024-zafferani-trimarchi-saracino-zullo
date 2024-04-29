package it.polimi.ingsw.gc28.network.messages.client;

import it.polimi.ingsw.gc28.controller.GameController;

import java.rmi.RemoteException;

public class MsgDrawnVisibleGameCard extends MessageC2S{
    String playerName;
    String cardId;
    public MsgDrawnVisibleGameCard(String playerName, String gameId, String cardId) {
        super(gameId);
        this.playerName = playerName;
        this.cardId = cardId;
    }

    @Override
    public void execute(GameController controller) throws RemoteException {
        controller.drawCard(playerName, cardId);
    }
}
