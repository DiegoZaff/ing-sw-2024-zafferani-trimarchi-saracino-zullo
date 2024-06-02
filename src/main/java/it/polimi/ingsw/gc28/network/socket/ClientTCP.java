package it.polimi.ingsw.gc28.network.socket;

import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.network.messages.client.*;
import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;
import it.polimi.ingsw.gc28.view.MessageUtils;
import it.polimi.ingsw.gc28.view.gui.GuiCallable;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;

public class ClientTCP implements GuiCallable {
    final ObjectInputStream input;
    final ServerProxy server;
    String userName;
    String gameId;



    protected ClientTCP(ObjectInputStream input, ObjectOutputStream output) {
        this.input = input;
        this.server = new ServerProxy(output);
    }

    private void run(boolean isCli) throws RemoteException {
        new Thread(() -> {
            try {
                runVirtualServer();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();

        if(isCli){
            runCli();
        }
//        else{
//            runGui();
//        }
    }

//    private void runGui(){
//        Application.launch(GuiApplication.class);
//    }

    /**
     * This continually listens for messages coming from the server.
     */
    private void runVirtualServer() {
        try{
            MessageS2C receivedObj = null;
            while ((receivedObj = (MessageS2C) input.readObject()) != null) {
                //System.out.println("Received message from server: " + receivedObj);

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

    private void runCli() throws RemoteException {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String line = scan.nextLine();

            String[] commands = line.split(" ");
            ArrayList<String> commandsList = new ArrayList<>(Arrays.asList(commands));

            if (commandsList.isEmpty()) {
                System.out.println("Give me a valid command plz.");
                continue;
            }


            boolean complete = MessageUtils.showSomething(commandsList);

            if (complete)
            {
                continue;
            }



                Optional<MessageC2S> message;

                String gameId = GameManagerClient.getInstance().getGameId();
                String userName = GameManagerClient.getInstance().getPlayerName();

                message = MessageUtils.createMessage(commandsList, null, gameId, userName); //non so se sia corretto,
                // in precedenza non veniva passato nulla come client e ho fatto
                // in questo modo che passo null, se va passato qualcosa
                // non ho idea di cosa sia e di come si passi senza modificare
                // tutto il metodo


                if (message.isPresent()) {
                    MessageC2S messageToSend = message.get();

                    if (messageToSend.getType().equals(MessageTypeC2S.CREATE_GAME) || messageToSend.getType().equals(MessageTypeC2S.JOIN_GAME)) {
                        if (GameManagerClient.getInstance().canICreateOrJoinAGame()) {
                            server.sendMessage(messageToSend);
                        }
                    }
                    else
                    {
                        server.sendMessage(messageToSend);
                    }
                }
        }
    }

    public static ClientTCP startClientSocket (String host, int port, boolean isCli) throws IOException {
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

            ClientTCP client = new ClientTCP(socketRx, socketTx);

            client.run(isCli);

            // TODO : make client implement same interface of rmiClient so that we can establish connection for GUI
            // TODO : and call methods on it to send messages to server.
            return client;
        }catch (IOException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void closeConnection() {
        server.closeConnection();
    }

    @Override
    public void sendMessageToServer(MessageC2S messageToSend) {
        if (messageToSend.getType().equals(MessageTypeC2S.CREATE_GAME) || messageToSend.getType().equals(MessageTypeC2S.JOIN_GAME)) {
            if (GameManagerClient.getInstance().canICreateOrJoinAGame()) {
                server.sendMessage(messageToSend);
            }
        }
        else
        {
            server.sendMessage(messageToSend);
        }
    }
}