package it.polimi.ingsw.gc28.network.messages.client;

import it.polimi.ingsw.gc28.controller.GameController;
import it.polimi.ingsw.gc28.model.errors.types.FailedActionManaged;

import java.rmi.RemoteException;

public class MsgPingC2S extends MessageC2S{
    public MsgPingC2S(MessageTypeC2S messageType) {
        super(messageType);
    }

    @Override
    public void execute(GameController controller) throws RemoteException, FailedActionManaged {

    }
}
