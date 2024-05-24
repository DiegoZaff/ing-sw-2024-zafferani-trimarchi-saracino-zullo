package it.polimi.ingsw.gc28.network.messages.client;

import it.polimi.ingsw.gc28.controller.GameController;
import it.polimi.ingsw.gc28.model.errors.types.FailedActionManaged;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;

import java.rmi.RemoteException;

public class MsgReconnect extends MessageC2S{
    private final VirtualView client;
    private final String playerName;
    public MsgReconnect(String gameId, VirtualView client, String playerName) {
        super(MessageTypeC2S.RECONNECT, gameId);
        this.client = client;
        this.playerName = playerName;
    }

    @Override
    public void execute(GameController controller) throws RemoteException, FailedActionManaged {
        boolean isSuccessful = controller.reconnect(playerName, client);

        if(!isSuccessful){
            throw new FailedActionManaged("Reconnection error was managed.");
        }

    }

    public String getPlayerName() {
        return playerName;
    }

    public VirtualView getClient() {
        return client;
    }
}
