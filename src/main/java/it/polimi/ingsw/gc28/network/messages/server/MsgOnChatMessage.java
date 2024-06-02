package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.GameRepresentation;
import it.polimi.ingsw.gc28.view.utils.InformationType;
import it.polimi.ingsw.gc28.view.utils.SnackBarMessage;

public class MsgOnChatMessage extends MessageS2C{
    private final GameRepresentation gameRepresentation;


    public MsgOnChatMessage(GameRepresentation gameRep){
        super(MessageTypeS2C.CHAT);
        this.gameRepresentation = gameRep;
    }

    @Override
    public void update(GameManagerClient gameManagerClient, boolean isCli)  {
        gameManagerClient.setCurrentRepresentation(gameRepresentation);

        if(!isCli){
            // TODO : aggiungi informazioni su chi ha inviato un messaggio, oltre alla gameRepresentation, e se è globale o privato
            SnackBarMessage msg = new SnackBarMessage("Player X has sent you a message", InformationType.CHAT_MESSAGE);
            gameManagerClient.updateSnackBarListener(msg);
        }
    }
}
