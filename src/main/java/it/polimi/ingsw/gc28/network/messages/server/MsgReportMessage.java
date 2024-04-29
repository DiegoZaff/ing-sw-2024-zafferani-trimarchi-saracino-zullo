package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.View.GameManagerClient;


public class MsgReportMessage extends MessageS2C{

    private final String details;
     public MsgReportMessage(String details){
         this.details = details;
     }

    @Override
    public void update(GameManagerClient gameManagerClient)  {
        gameManagerClient.writeInConsole(details);
    }
}
