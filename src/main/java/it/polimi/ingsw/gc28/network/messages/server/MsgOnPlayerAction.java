package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.View.GameRepresentation;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;

import java.io.IOException;

public class MsgOnPlayerAction extends MessageS2C{

    GameRepresentation gameRepresentation;

    public MsgOnPlayerAction(GameRepresentation gameRep){
        this.gameRepresentation = gameRep;
    }

    @Override
    public void update(GameRepresentation clientRepresentation) throws IOException {
        clientRepresentation.update(gameRepresentation);
    }
}
