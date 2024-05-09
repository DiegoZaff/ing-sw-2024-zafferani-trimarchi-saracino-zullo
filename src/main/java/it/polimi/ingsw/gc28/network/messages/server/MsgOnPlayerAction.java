package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.GameRepresentation;

public class MsgOnPlayerAction extends MessageS2C{

    private final GameRepresentation gameRepresentation;


    public MsgOnPlayerAction(GameRepresentation gameRep){
        this.gameRepresentation = gameRep;
    }

    @Override
    public void update(GameManagerClient gameManagerClient)  {
        gameManagerClient.setCurrentRepresentation(gameRepresentation);
    }
}
