package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.View.GameRepresentation;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;

import java.io.IOException;

public class MsgOnGameJoined extends MessageS2C{

    String playerName;
    int playersLeftToJoin;

    public MsgOnGameJoined(String playerName, int playersLeftToJoin){
        this.playerName = playerName;
        this.playersLeftToJoin = playersLeftToJoin;
    }

    @Override
    public void update(GameRepresentation gameRepresentation) throws IOException {

    }
}
