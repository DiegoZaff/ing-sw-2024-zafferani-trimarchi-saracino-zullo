package it.polimi.ingsw.gc28.network.messages.client;

import it.polimi.ingsw.gc28.controller.GameController;
import it.polimi.ingsw.gc28.model.errors.types.FailedActionManaged;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;

import java.rmi.RemoteException;
/**
 * message sent from the client to the server to reconnect to the game
 */
public class MsgReconnect extends MessageC2S{
    private  VirtualView client;
    private final String playerName;
    public MsgReconnect(String gameId, String playerName) {
        super(MessageTypeC2S.RECONNECT, gameId);
        this.playerName = playerName;
    }

    public MsgReconnect(String gameId, String playerName, VirtualView client) {
        super(MessageTypeC2S.RECONNECT, gameId);
        this.playerName = playerName;
        this.client = client;
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

    public void setClient(VirtualView client){
        this.client = client;
    }

    @Override
    public void attachVirtualView(VirtualView client){
        setClient(client);
    }
}
