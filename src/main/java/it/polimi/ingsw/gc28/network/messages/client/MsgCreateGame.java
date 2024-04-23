package it.polimi.ingsw.gc28.network.messages.client;

import it.polimi.ingsw.gc28.controller.GameController;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;

public class MsgCreateGame extends MessageC2S{

    VirtualView client;
    String userName;
    int numberOfPlayers;

    public MsgCreateGame(String gameId, VirtualView client, String userName, int numberOfPlayers) {
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
}
