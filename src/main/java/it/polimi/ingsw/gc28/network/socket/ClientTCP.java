package it.polimi.ingsw.gc28.network.socket;

import it.polimi.ingsw.gc28.network.messages.client.MessageC2S;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ClientTCP {
    final ObjectInputStream input;
    final ServerProxy server;

    protected ClientTCP(ObjectInputStream input, ObjectOutputStream output) {
        this.input = input;
        this.server = new ServerProxy(output);
    }

    private void run() throws RemoteException {
        new Thread(() -> {
            try {
                runVirtualServer();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
        runCli();
    }

    /**
     * This continually listens for messages coming from the server.
     */
    private void runVirtualServer() {
        try{
            MessageC2S receivedObj = null;
            while ((receivedObj = (MessageC2S) input.readObject()) != null) {
                System.out.println("Received message from server: " + receivedObj);

                // TODO : implement handling of message.
                // TODO : we could create a GameRepresentation Singleton Class
                // TODO : which serves as local representation of the game, which is updated upon
                // TODO : receiving a new message from the server.
                // TODO : also print stuff upon receiving a message.
            }
        }catch (ClassNotFoundException e) {
            // Handle class not found exception
            System.err.println("Error: Class not found while deserializing object.");
        } catch (EOFException e) {
            // Server has closed the connection
            System.out.println("Server disconnected.");
        } catch (IOException e) {
            // Handle IO exception
            System.err.println("Error reading object from input stream.");
        }
    }

    private void runCli() {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String line = scan.nextLine();

            String[] commands = line.split(" ");
            ArrayList<String> commandsList = new ArrayList<>(Arrays.asList(commands));

            if(commandsList.size() < 2){
                System.err.println("Give me a valid command plz.");
                continue;
            }

            // TODO: construct message to send to server from the commands given.
            //c'è gia in rmi vanno unificati i due metodi;

        }
    }

    public static void startClientSocket(String host, int port) throws IOException {
        Socket serverSocket = new Socket(host, port);
        ObjectInputStream socketRx = null;
        ObjectOutputStream socketTx = null;
        try{
            socketTx = new ObjectOutputStream(serverSocket.getOutputStream());
            socketRx = new ObjectInputStream(serverSocket.getInputStream());

            new ClientTCP(socketRx, socketTx).run();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
