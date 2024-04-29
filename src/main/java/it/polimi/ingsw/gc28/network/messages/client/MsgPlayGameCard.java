package it.polimi.ingsw.gc28.network.messages.client;

import it.polimi.ingsw.gc28.controller.GameController;
import it.polimi.ingsw.gc28.model.Coordinate;

import java.rmi.RemoteException;

public class MsgPlayGameCard extends MessageC2S{
    String playerName;
    String cardId;
    boolean isFront;
    Coordinate coordinate;
    public MsgPlayGameCard(String playerName, String cardId, String gameId, boolean isFront, Coordinate coordinate) {
        super(gameId);
        this.playerName = playerName;
        this.cardId = cardId;
        this.isFront = isFront;
        this.coordinate = coordinate;
    }

    @Override
    public void execute(GameController controller) throws RemoteException {
        controller.playCard(playerName, cardId, isFront, coordinate);
    }
}
