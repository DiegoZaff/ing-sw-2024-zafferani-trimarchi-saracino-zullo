package it.polimi.ingsw.gc28.network.socket;

import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;

import java.io.IOException;

/**
 * These are methods called from the server, that will act on the client
 */

// TODO : unifica con quella RMI - gerarchia VirtualView -> VirtualViewRMI - VirtualViewSocket
// TODO : questi metodi costruiscono messaggi e li inviano.

public interface VirtualView {
    public void sendMessage(MessageS2C message);
}
