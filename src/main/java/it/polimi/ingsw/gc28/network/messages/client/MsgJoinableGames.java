package it.polimi.ingsw.gc28.network.messages.client;

import it.polimi.ingsw.gc28.controller.GameController;
import it.polimi.ingsw.gc28.model.errors.types.FailedActionManaged;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;

import java.rmi.RemoteException;
/**
 * message sent from the client to the server to ask for joinable games
 */
public class MsgJoinableGames extends  MessageC2S{
    private  VirtualView client;
    public MsgJoinableGames() {
        super(MessageTypeC2S.JOINABLE_GAMES);
    }

    @Override
    public void execute(GameController controller) throws RemoteException, FailedActionManaged {

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
