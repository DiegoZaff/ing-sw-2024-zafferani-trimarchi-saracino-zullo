package it.polimi.ingsw.gc28.network.socket;

import it.polimi.ingsw.gc28.View.GameManagerClient;
import it.polimi.ingsw.gc28.network.messages.client.MsgCreateGame;
import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ClientTCP {
    final ObjectInputStream input;
    final ServerProxy server;
    String userName;

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
            MessageS2C receivedObj = null;
            while ((receivedObj = (MessageS2C) input.readObject()) != null) {
                System.out.println("Received message from server: " + receivedObj);

                GameManagerClient.getInstance().addMessageToQueue(receivedObj);
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

            if (commandsList.size() < 2) {
                System.err.println("Give me a valid command plz.");
                continue;
            }

            String action = commandsList.getFirst();

            switch (action) {
                case "createGame":
                    if (commandsList.size() != 3) {
                        System.err.println("Invalid format");
                        return;
                    }

                    userName = commandsList.get(1);

                    //qui va controllato che venga inserito un numero compreso tra 2 e 4;
                    Integer nPlayers = null;
                    try {
                        nPlayers = Integer.parseInt(commandsList.get(2));
                    } catch (NumberFormatException e) {
                        System.err.println(e.getMessage());
                        return;
                    }

                    if (nPlayers < 2 || nPlayers > 4) {
                        System.out.println("Select a number of player between 2 and 4");
                        break;
                    }

                    // TODO : remember to attach client inside clientHandler
                    MsgCreateGame message = new MsgCreateGame(null, userName, nPlayers, null);
                    server.sendMessage(message);

                    break;
                case "joinGame":


                    // TODO: construct message to send to server from the commands given.
                    //c'è gia in rmi vanno unificati i due metodi;

            }
        }
    }

    public static void startClientSocket (String host, int port) throws IOException {
        Socket serverSocket = null;
        try{
            serverSocket = new Socket(host, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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