package it.polimi.ingsw.gc28.network.socket;


import it.polimi.ingsw.gc28.controller.GameController;
import it.polimi.ingsw.gc28.network.messages.client.MessageC2S;
import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHandler implements VirtualView {
    final ServerTCP server;
    final ObjectInputStream input;
    final VirtualView view;
    final Socket clientSocket;

    public ClientHandler(ServerTCP server, ObjectInputStream input, ObjectOutputStream output, Socket clientSocket) {
        this.server = server;
        this.input = input;
        this.view = new ClientProxy(output);
        this.clientSocket = clientSocket;
    }

    public void runVirtualView() throws IOException {

        try {
            MessageC2S receivedObj = null;
            while ((receivedObj = (MessageC2S) input.readObject()) != null) {
                System.out.println("Received message from client: " + receivedObj);

                // TODO: handle message received from the client
                // TODO: forward message to GamesManager so that the proper controller is attached to this message.
            }
        } catch (EOFException e) {
            // Client has closed the connection
            System.out.println("Client disconnected.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            input.close();
            clientSocket.close();
        }
    }

    @Override
    public void sendMessage(MessageS2C message) {
        synchronized (this.view){
            this.view.sendMessage(message);
        }
    }
}
