package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.view.GameManagerClient;
/**
 * message sent from the server to the client to notify that the game has terminated
 */
public class MsgOnGameTermination extends MessageS2C{
    public MsgOnGameTermination(MessageTypeS2C type) {
        super(type);
    }

    @Override
    public void update(GameManagerClient gameManagerClient, boolean isCli) {
        gameManagerClient.terminateGame();
    }
}
