package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.GameRepresentation;
/**
 * message sent from the server to the client to notify an action from a player
 */
public class MsgOnPlayerAction extends MessageS2C{

    private final GameRepresentation gameRepresentation;


    public MsgOnPlayerAction(GameRepresentation gameRep){
        super(MessageTypeS2C.PLAYER_ACTION);
        this.gameRepresentation = gameRep;
    }

    @Override
    public void update(GameManagerClient gameManagerClient, boolean isCli)  {
        gameManagerClient.setCurrentRepresentation(gameRepresentation);
    }
}
