package it.polimi.ingsw.gc28.network.socket;

import it.polimi.ingsw.gc28.controller.GameController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ServerTCP extends Thread {
    final ServerSocket serverSocket;
    final List<ClientHandler> clients = new ArrayList<>();

    Map<ClientHandler,GameController> mapController = new HashMap<>();

    public ServerTCP(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    public void addController(ClientHandler client, GameController controller){
        mapController.put(client,controller);
    }



    @Override
    public void run() {
        Socket clientSocket = null;
        try{
            while ((clientSocket = this.serverSocket.accept()) != null) {
                System.out.printf("Connection accepted through socket TCP: %s %s%n",clientSocket.getInetAddress(),  clientSocket.getPort());
                ObjectInputStream socketRx = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream socketTx = new ObjectOutputStream(clientSocket.getOutputStream());

                ClientHandler handler = new ClientHandler(this, socketRx, socketTx, clientSocket);

                clients.add(handler);
                new Thread(() -> {
                    try {
                        handler.runVirtualView();
                    } catch (IOException e) {
                        try {
                            if (mapController.get(handler) != null){
                                mapController.get(handler).notifyGameTermination();
                            }
                        } catch (RemoteException ex) {
                            throw new RuntimeException(ex);
                        }

                    }
                }).start();
            }
        }catch (IOException e){
            System.err.println("Error occurred while waiting for a connection");
        }

    }
}