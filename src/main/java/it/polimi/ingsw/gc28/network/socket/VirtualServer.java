package it.polimi.ingsw.gc28.network.socket;

import it.polimi.ingsw.gc28.network.messages.client.MessageC2S;
import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;

// TODO : questi metodo mandano messaggi
public interface VirtualServer {

    public void sendMessage(MessageC2S message);

}
