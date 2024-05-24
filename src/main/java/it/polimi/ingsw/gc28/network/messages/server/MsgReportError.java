package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.view.GameManagerClient;


public class MsgReportError extends MessageS2C{

    private final String details;
    public MsgReportError(String details){
        super(MessageTypeS2C.ERROR);
        this.details = details;
    }

    @Override
    public void update(GameManagerClient gameManagerClient)  {
        gameManagerClient.writeInConsole(details);
    }
}
