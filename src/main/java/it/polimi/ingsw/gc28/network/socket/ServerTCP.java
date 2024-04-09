package it.polimi.ingsw.gc28.network.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class ServerTCP extends Thread {
    final ServerSocket listenSocket;
    final List<ClientHandler> clients = new ArrayList<>();

    public ServerTCP(int port) throws IOException {
        this.listenSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        Socket clientSocket = null;
        try{
            while ((clientSocket = this.listenSocket.accept()) != null) {
                System.out.printf("Connection accepted through socket TCP: %s %s%n",clientSocket.getInetAddress(),  clientSocket.getPort());
                ObjectInputStream socketRx = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream socketTx = new ObjectOutputStream(clientSocket.getOutputStream());

                ClientHandler handler = new ClientHandler(this, socketRx, socketTx, clientSocket);

                clients.add(handler);
                new Thread(() -> {
                    try {
                        handler.runVirtualView();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        }catch (IOException e){
            System.err.println("Error occurred while waiting for a connection");
        }

    }
}