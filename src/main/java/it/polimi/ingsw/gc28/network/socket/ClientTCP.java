package it.polimi.ingsw.gc28.network.socket;

import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.network.messages.client.*;
import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;
import it.polimi.ingsw.gc28.view.MessageUtils;
import it.polimi.ingsw.gc28.view.gui.GuiCallable;
import it.polimi.ingsw.gc28.view.utils.InformationType;
import it.polimi.ingsw.gc28.view.utils.SnackBarMessage;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ClientTCP implements GuiCallable {

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private String host;
    private  int port;
    private ObjectInputStream input;
    private ServerProxy server;

    boolean isCli;

    private Boolean serverDown = false;



    protected ClientTCP(ObjectInputStream input, ObjectOutputStream output, boolean isCli) {
        this.isCli = isCli;
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

        new Thread(() -> {
            try {
                sendPing();
            } catch (Exception e) {
                System.out.println("Unable to reach the server");
                serverDown = true;
                try {
                    reconnect();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    this.run();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }).start();

        // start cli
        if(isCli){
            runCli();
        }
    }


    private void sendPing () throws InterruptedException, IOException {
        MessageC2S ping = new MsgPingC2S(MessageTypeC2S.PING);
        while (!serverDown){
            TimeUnit.SECONDS.sleep(5);
            server.sendMessage(ping);
        }
    }

    private void reconnect() throws IOException {
        SnackBarMessage msgSnackBar = new SnackBarMessage("Trying to reconnect...", InformationType.ERROR);
        boolean flag = true;
        while (flag){
            try{
                TimeUnit.SECONDS.sleep(5);
                if(isCli){
                    System.out.println("trying to reconnect...");
                }else{
                    GameManagerClient.getInstance().updateSnackBarListener(msgSnackBar);
                }
                Socket s = new Socket(host,port);
                ObjectOutputStream socketTx = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream socketRx = new ObjectInputStream(s.getInputStream());

                this.server = new ServerProxy(socketTx);
                this.input = socketRx;

                flag = false;
            }catch (Exception ignored){
//                System.err.println(ignored.getMessage());
            }

        }

        if (GameManagerClient.getInstance().getCanBeRecreated()) {
            MsgReconnect msg = new MsgReconnect(GameManagerClient.getInstance().getGameId(), GameManagerClient.getInstance().getPlayerName());
            server.sendMessage(msg);
        }

        serverDown = false;
    }


    /**
     * This continually listens for messages coming from the server.
     */
    private void runVirtualServer() {
        try{
            MessageS2C receivedObj = null;
            while ((receivedObj = (MessageS2C) input.readObject()) != null && !serverDown) {
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
            //System.err.println("Error reading object from input stream.");
        }
    }

    private void runCli() throws RemoteException {
        Scanner scan = new Scanner(System.in);
        while (!serverDown) {
            System.out.print("> ");
            String line = scan.nextLine();

            String[] commands = line.split(" ");
            ArrayList<String> commandsList = new ArrayList<>(Arrays.asList(commands));

            if (commandsList.isEmpty()) {
                System.out.println("Give me a valid command.");
                continue;
            }


            boolean complete = MessageUtils.showSomething(commandsList);

            if (complete) {
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

                sendMessageToServer(messageToSend);
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

            ClientTCP client = new ClientTCP(socketRx, socketTx, isCli);

            client.setHost(host);
            client.setPort(port);

            client.run();

            return client;
        }catch (IOException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendMessageToServer(MessageC2S messageToSend) {
        if(!serverDown){
            try {
                if (messageToSend.getType().equals(MessageTypeC2S.CREATE_GAME) || messageToSend.getType().equals(MessageTypeC2S.JOIN_GAME)) {
                    if (GameManagerClient.getInstance().canICreateOrJoinAGame()) {
                        server.sendMessage(messageToSend);
                    }
                } else {
                    if(messageToSend.getType().equals(MessageTypeC2S.PLAY_CARD) || messageToSend.getType().equals(MessageTypeC2S.DRAW_CARD_DECK)
                    || messageToSend.getType().equals(MessageTypeC2S.DRAW_CARD_VISIBLE)){
                        if(GameManagerClient.getInstance().getGameId() == null){
                            if(isCli){
                                System.out.println("Looks like you're not in a game!");
                            }else{
                                SnackBarMessage msg = new SnackBarMessage("Looks like you're not in a game!", InformationType.ERROR);
                                GameManagerClient.getInstance().updateSnackBarListener(msg);

                            }
                        }
                    }
                    server.sendMessage(messageToSend);
                }
            }catch (IOException e){
                logSnackbarError();
                serverDown = true;
            }
        }else{
            logSnackbarError();
        }
    }

    private void logSnackbarError(){
        if(!isCli){
            SnackBarMessage msg = new SnackBarMessage("Can't talk to server...", InformationType.ERROR);
            GameManagerClient.getInstance().updateSnackBarListener(msg);
        }else{
            System.err.println("unable to reach the server");        }
    }
}