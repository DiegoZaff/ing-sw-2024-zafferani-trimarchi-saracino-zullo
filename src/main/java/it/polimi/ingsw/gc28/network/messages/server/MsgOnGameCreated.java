package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.View.GameRepresentation;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;

import java.io.IOException;

public class MsgOnGameCreated extends MessageS2C{

    String gameId;
    String playerName;
    int playersLeftToJoin;

    public MsgOnGameCreated(String gameId, String playerName, int playersLeftToJoin){
        this.gameId = gameId;
        this.playerName = playerName;
        this.playersLeftToJoin = playersLeftToJoin;
    }

    @Override
    public void update(GameRepresentation gameRepresentation) throws IOException {

    }
}
