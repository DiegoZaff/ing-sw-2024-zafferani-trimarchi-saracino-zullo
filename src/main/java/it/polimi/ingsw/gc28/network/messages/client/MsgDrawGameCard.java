package it.polimi.ingsw.gc28.network.messages.client;

import it.polimi.ingsw.gc28.controller.GameController;

import java.rmi.RemoteException;

public class MsgDrawGameCard extends MessageC2S{
    String playerName;
    String gameId;
    boolean fromGoldDeck;
    public MsgDrawGameCard(String playerName, String gameId, boolean fromGoldDeck) {
        super(gameId);
        this.playerName = playerName;
        this.fromGoldDeck = fromGoldDeck;
    }
    @Override
    public void execute(GameController controller) throws RemoteException {
        controller.drawCard(playerName, fromGoldDeck);
    }
}
