package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.view.GameManagerClient;
/**
 * message sent from the server to the client to ping the client
 */
public class MsgPingS2c extends MessageS2C{

    public MsgPingS2c(MessageTypeS2C type) {
        super(type);
    }

    @Override
    public void update(GameManagerClient gameManagerClient, boolean isCli) {
        if (isCli){

        }
    }
}
