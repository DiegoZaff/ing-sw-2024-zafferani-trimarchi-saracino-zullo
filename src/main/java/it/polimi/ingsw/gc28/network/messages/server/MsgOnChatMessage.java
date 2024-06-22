package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.GameRepresentation;
import it.polimi.ingsw.gc28.view.utils.InformationType;
import it.polimi.ingsw.gc28.view.utils.SnackBarMessage;
/**
 * message sent from the server to the client to send a chat message
 */
public class MsgOnChatMessage extends MessageS2C{
    private final GameRepresentation gameRepresentation;
    private final String sender;
    private final String receiver;
    private final boolean isPrivate;


    public MsgOnChatMessage(GameRepresentation gameRep, String sender, String receiver, boolean isPrivate){
        super(MessageTypeS2C.CHAT);
        this.gameRepresentation = gameRep;
        this.sender = sender;
        this.receiver = receiver;
        this.isPrivate = isPrivate;
    }

    @Override
    public void update(GameManagerClient gameManagerClient, boolean isCli)  {
        gameManagerClient.setCurrentRepresentation(gameRepresentation);

        if(!isCli){
            SnackBarMessage msg;
            if(isPrivate){
                msg = new SnackBarMessage(sender + "has sent you a private message", InformationType.CHAT_MESSAGE);
            } else {
                msg = new SnackBarMessage(sender + "has sent a global message", InformationType.CHAT_MESSAGE);
            }
            gameManagerClient.updateSnackBarListener(msg);
            gameManagerClient.updateListeners(this);
        }
    }

    public GameRepresentation getGameRepresentation() {
        return gameRepresentation;
    }
    public boolean isPrivate() {
        return isPrivate;
    }
    public String getSender() {
        return sender;
    }

    public String getReceiver(){
        return receiver;
    }
}
