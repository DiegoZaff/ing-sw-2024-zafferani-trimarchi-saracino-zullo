package it.polimi.ingsw.gc28.network.socket;

import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

public class ClientProxy implements VirtualView {
    final ObjectOutputStream output;

    public ClientProxy(ObjectOutputStream output) {
        this.output = output;
    }

    @Override
    public void sendMessage(MessageS2C message)  {
        try{
            output.writeObject(message);
        }catch (IOException e){
            System.err.println("Error writing object to output stream: " + e.getMessage());
        }
    }

}
