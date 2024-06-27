package it.polimi.ingsw.gc28.network.messages.client;

import it.polimi.ingsw.gc28.controller.GameController;
import it.polimi.ingsw.gc28.model.errors.types.FailedActionManaged;

import java.rmi.RemoteException;

public class MsgLeaveGame extends MessageC2S{
    public MsgLeaveGame() {
        super(MessageTypeC2S.LEAVEGAME);
    }

    @Override
    public void execute(GameController controller) throws RemoteException, FailedActionManaged {
        controller.notifyGameTermination();
        controller.deleteGameAndBackUp();
    }
}
