package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.GameRepresentation;

public class MsgOnChatMessage extends MessageS2C{
    private final GameRepresentation gameRepresentation;


    public MsgOnChatMessage(GameRepresentation gameRep){
        super(MessageTypeS2C.CHAT);
        this.gameRepresentation = gameRep;
    }

    @Override
    public void update(GameManagerClient gameManagerClient, boolean isCli)  {
        gameManagerClient.setCurrentRepresentation(gameRepresentation);
    }
}
