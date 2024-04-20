package it.polimi.ingsw.gc28.network.socket;

import it.polimi.ingsw.gc28.network.messages.client.MessageC2S;

// TODO : questi metodo mandano messaggi
public interface VirtualServer2 {

    public void sendMessage(MessageC2S message);

}
