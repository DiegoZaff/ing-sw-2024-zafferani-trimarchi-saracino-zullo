package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.view.GameManagerClient;


public class MsgReportError extends MessageS2C{

    private final String details;
    public MsgReportError(String playerName, String details){
        this.details = details;
    }

    @Override
    public void update(GameManagerClient gameManagerClient)  {
        gameManagerClient.writeInConsole(details);
    }
}
