package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.view.GameManagerClient;

/**
 * message sent from the server to the client to report a message
 */
public class MsgReportMessage extends MessageS2C{

    private final String details;
     public MsgReportMessage(String details){
         super(MessageTypeS2C.REPORT);
         this.details = details;
     }

    @Override
    public void update(GameManagerClient gameManagerClient, boolean isCli)  {
        gameManagerClient.writeInConsole(details);
    }
}
