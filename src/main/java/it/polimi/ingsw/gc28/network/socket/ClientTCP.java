package it.polimi.ingsw.gc28.network.socket;

import it.polimi.ingsw.gc28.network.rmi.VirtualView;
import it.polimi.ingsw.gc28.view.gui.GuiApplication;
import it.polimi.ingsw.gc28.network.rmi.VirtualStub;
import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.network.messages.client.*;
import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;
import it.polimi.ingsw.gc28.view.MessageToServer;
import it.polimi.ingsw.gc28.view.ShowSomething;
import javafx.application.Application;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;

public class ClientTCP {
    final ObjectInputStream input;
    final ServerProxy server;
    String userName;
    String gameId;
    MessageToServer messageToServer = MessageToServer.getInstance();
    VirtualStub virtualGameStub;

    ShowSomething showSomething = ShowSomething.getInstance();


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


            boolean complete = showSomething.showSomething(commandsList);

            if (complete)
            {
                continue;
            }



                Optional<MessageC2S> message;

                String gameId = GameManagerClient.getInstance().getGameId();
                String userName = GameManagerClient.getInstance().getPlayerName();

                message = messageToServer.createMessage(commandsList, null, gameId, userName); //non so se sia corretto,
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
                    } else {
                        if (virtualGameStub == null) {
                            System.out.println("Looks like you're not in a game!");
                        }

                        virtualGameStub.sendMessage(messageToSend);
                    }
                }


/*
            String action = commandsList.getFirst();

            switch (action) {
                case "createGame": {
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
                }
                case "joinGame": {

                    if (commandsList.size() != 3) {
                        System.err.println("Invalid format");
                        return;
                    }

                    String gameIdToJoin = commandsList.get(1);
                    userName = commandsList.get(2);

                    MsgJoinGame JGMess = new MsgJoinGame(null, gameIdToJoin, userName);
                    server.sendMessage(JGMess);

                    break;
                }
                case "chooseObj": {

                    if (commandsList.size() != 3) {
                        System.err.println("Invalid format");
                        return;
                    }

                    String cardId = commandsList.get(1);
                    gameId = commandsList.get(2);

                    MsgChooseObjective COMess = new MsgChooseObjective(userName, gameId, cardId);
                    server.sendMessage(COMess);

                    break;
                }
                case "playCard": {

                    if (commandsList.size() != 5) {
                        System.err.println("Invalid format");
                        return;
                    }

                    String cardId = commandsList.get(1);

                    String isFrontString = commandsList.get(2);

                    if (!isFrontString.equals("up") && !isFrontString.equals("down")) {
                        System.err.println("Invalid isFront format");
                        return;
                    }

                    boolean isFront = isFrontString.equals("up");

                    Integer x = null;
                    Integer y = null;

                    try {
                        x = Integer.parseInt(commandsList.get(3));
                        y = Integer.parseInt(commandsList.get(4));
                    } catch (NumberFormatException e) {
                        System.err.println(e.getMessage());
                        return;
                    }


                    MsgPlayGameCard PGCMess = new MsgPlayGameCard( userName, cardId, gameId, isFront, new Coordinate(x,y));
                    server.sendMessage(PGCMess);

                    break;
                }
                default:
                    System.err.println("Invalid First Argument!");
                    break;
                    
            }*/
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
}