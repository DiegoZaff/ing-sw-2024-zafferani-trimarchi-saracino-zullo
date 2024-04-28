package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.View.GameRepresentation;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;

import java.io.IOException;
import java.util.ArrayList;

public class MsgOnGameStarted extends MessageS2C{

    GameRepresentation gameRepresentation;

    public MsgOnGameStarted(GameRepresentation gameRepresentation){
        this.gameRepresentation = gameRepresentation;
    }

    @Override
    public void update(GameRepresentation clientRepresentation) throws IOException {
        clientRepresentation.update(gameRepresentation);
    }
}
