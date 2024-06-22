package it.polimi.ingsw.gc28.network.messages.client;

import it.polimi.ingsw.gc28.controller.GameController;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;
/**
 * message sent from the client to the server to create a game
 */
public class MsgCreateGame extends MessageC2S{
    private  VirtualView client;

    private final String userName;
    private final int numberOfPlayers;

    public MsgCreateGame(String gameId, String userName, int numberOfPlayers) {
        super(MessageTypeC2S.CREATE_GAME, gameId);
        this.userName = userName;
        this.numberOfPlayers = numberOfPlayers;
    }

    @Override
    public void execute(GameController controller) {

    }

    public int getNumberOfPlayers(){
        return numberOfPlayers;
    }

    public String getUserName(){
        return userName;
    }

    public VirtualView getClient(){
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
