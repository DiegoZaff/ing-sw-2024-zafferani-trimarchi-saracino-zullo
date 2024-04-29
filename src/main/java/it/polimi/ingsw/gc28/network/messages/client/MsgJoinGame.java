package it.polimi.ingsw.gc28.network.messages.client;

import it.polimi.ingsw.gc28.controller.GameController;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;

import java.rmi.RemoteException;

public class MsgJoinGame extends MessageC2S{
    VirtualView client;
    String userName;
    public MsgJoinGame(VirtualView client, String gameId, String userName) {
        super(gameId);
        this.client = client;
        this.userName = userName;

    }

    @Override
    public void execute(GameController controller) throws RemoteException {
        controller.addPlayerToGame(userName, client, true);
    }
}
