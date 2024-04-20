package it.polimi.ingsw.gc28.network.socket;

import it.polimi.ingsw.gc28.network.messages.client.MessageC2S;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class ServerProxy implements VirtualServer2 {
    final ObjectOutputStream output;

    public ServerProxy(ObjectOutputStream output) {
        this.output =output;
    }

    @Override
    public void sendMessage(MessageC2S message) {
        try{
            output.writeObject(message);
        }catch (IOException e){
            System.err.println("Error writing object to output stream: " + e.getMessage());
        }
    }
}
