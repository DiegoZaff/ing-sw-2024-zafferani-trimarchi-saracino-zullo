package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.utils.InformationType;
import it.polimi.ingsw.gc28.view.utils.SnackBarMessage;

/**
 * message sent from the server to the client to report an error
 */
public class MsgReportError extends MessageS2C{

    private final String details;
    public MsgReportError(String details){
        super(MessageTypeS2C.ERROR);
        this.details = details;
    }

    @Override
    public void update(GameManagerClient gameManagerClient, boolean isCli)  {
        if(isCli){
            gameManagerClient.writeInConsole(details);
        }else{
            SnackBarMessage msg = new SnackBarMessage(details, InformationType.ERROR);
            gameManagerClient.updateSnackBarListener(msg);
        }
    }
}
