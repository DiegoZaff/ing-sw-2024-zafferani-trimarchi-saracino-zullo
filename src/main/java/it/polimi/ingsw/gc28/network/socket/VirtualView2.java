package it.polimi.ingsw.gc28.network.socket;

import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;

/**
 * These are methods called from the server, that will act on the client
 */

// TODO : unifica con quella RMI - gerarchia VirtualView -> VirtualViewRMI - VirtualViewSocket
// TODO : questi metodi costruiscono messaggi e li inviano.

public interface VirtualView2 {
    public void sendMessage(MessageS2C message);
}

// i think we could eliminate this interface
