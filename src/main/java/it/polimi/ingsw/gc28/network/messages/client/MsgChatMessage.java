package it.polimi.ingsw.gc28.network.messages.client;

import it.polimi.ingsw.gc28.controller.GameController;

import java.rmi.RemoteException;

public class MsgChatMessage extends MessageC2S{
    protected MsgChatMessage(String gameId) {
        super(gameId);
    }

    @Override
    public void execute(GameController controller) throws RemoteException {

    }
}
