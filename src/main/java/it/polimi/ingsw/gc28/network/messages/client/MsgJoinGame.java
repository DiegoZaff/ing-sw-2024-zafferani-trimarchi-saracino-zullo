package it.polimi.ingsw.gc28.network.messages.client;

import it.polimi.ingsw.gc28.controller.GameController;
import it.polimi.ingsw.gc28.model.errors.types.FailedActionManaged;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;

import java.rmi.RemoteException;

public class MsgJoinGame extends MessageC2S{
    private  VirtualView client;

    public void setClient(VirtualView client) {
        this.client = client;
    }

    public VirtualView getClient(){
        return client;
    }

    private String userName;

    public String getUserName(){
        return userName;
    }

    public MsgJoinGame(VirtualView client, String gameId, String userName) {
        super(MessageTypeC2S.JOIN_GAME ,gameId);
        this.client = client;
        this.userName = userName;

    }

    @Override
    public void execute(GameController controller) throws RemoteException, FailedActionManaged {
        boolean isSuccessful = controller.addPlayerToGame(userName, client);

        if(!isSuccessful){
            throw new FailedActionManaged("Could not join game");
        }
    }
}
