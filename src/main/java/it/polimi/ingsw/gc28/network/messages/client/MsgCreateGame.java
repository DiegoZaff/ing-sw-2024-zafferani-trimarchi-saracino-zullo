package it.polimi.ingsw.gc28.network.messages.client;

import it.polimi.ingsw.gc28.controller.GameController;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;

public class MsgCreateGame extends MessageC2S{
    private final VirtualView client;

    private final String userName;
    private final int numberOfPlayers;

    public MsgCreateGame(String gameId, String userName, int numberOfPlayers, VirtualView client) {
        super(gameId);
        this.client = client;
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

}
