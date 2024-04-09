package it.polimi.ingsw.gc28.network.socket;

import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;

public interface VirtualServer {

    public void sendMessage(MessageS2C message);
//    public void connect(VirtualView client);
//
//    public void add(Integer number);
//
//    public void reset();
}
