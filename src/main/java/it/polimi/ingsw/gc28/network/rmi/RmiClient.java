package it.polimi.ingsw.gc28.network.rmi;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.util.*;

import it.polimi.ingsw.gc28.controller.GamesManager;
import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.MessageUtils;
import it.polimi.ingsw.gc28.network.messages.client.*;
import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;
import it.polimi.ingsw.gc28.view.gui.GuiCallable;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.TimeUnit;


public class RmiClient extends UnicastRemoteObject implements VirtualView, GuiCallable {
    VirtualServer server;

    VirtualStub virtualGameStub;

    static Registry registry;

    String id;


    String currentNickname;


    Boolean serverDown = false;



    protected RmiClient(VirtualServer server) throws RemoteException {
        this.server = server;
        this.id = UUID.randomUUID().toString();
    }

    private void run(boolean isCli) throws RemoteException {

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
                sendPing();
            } catch (InterruptedException | IOException e) {
                System.err.println("unable to reach the server!");
                serverDown = true;
                try {
                    this.reconnect();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    this.run(isCli);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }).start();


        if(isCli){
            runCli();
        }


    }
    private void sendPing () throws InterruptedException, IOException {
        MessageC2S ping = new MsgPingC2S(MessageTypeC2S.PING);
        while (!serverDown){
            TimeUnit.SECONDS.sleep(6);
            server.sendMessage(ping);
        }
    }

    private void reconnect() throws IOException {

        MsgReconnect msg = new MsgReconnect(GameManagerClient.getInstance().getGameId(), currentNickname, this);
        boolean flag = true;
        while (flag){
            try{
                TimeUnit.SECONDS.sleep(6);
                System.out.println("trying to reconnect...");
                VirtualServer server = (VirtualServer) registry.lookup("VirtualServer");

                this.server = server;

                server.sendMessage(msg);
                flag = false;
            }catch (Exception ignored){
            }
        }


        serverDown = false;

    }

    private void runCli() throws RemoteException {
        Scanner scan = new Scanner(System.in);
        while (!serverDown) {
            System.out.print("> ");
            String line = scan.nextLine();

            String[] commands = line.split(" ");
            ArrayList<String> commandsList = new ArrayList<>(Arrays.asList(commands));

            if (commandsList.isEmpty()) {
                System.out.println("Give me a valid command plz.");
                continue;
            }


            boolean complete = MessageUtils.showSomething(commandsList);

            if (complete) {
                continue;
            }

            Optional<MessageC2S> message;

            String gameId = GameManagerClient.getInstance().getGameId();
            String userName = GameManagerClient.getInstance().getPlayerName();

            message = MessageUtils.createMessage(commandsList, this, gameId, userName);

            if (message.isPresent()) {
                MessageC2S messageToSend = message.get();

                if(messageToSend.getType().equals(MessageTypeC2S.CREATE_GAME)){
                    this.currentNickname = commandsList.get(1);
                }
                if (messageToSend.getType().equals(MessageTypeC2S.JOIN_GAME)){
                    this.currentNickname = commandsList.get(2);
                }
                sendMessageToServer(messageToSend);
            }

        }
    }

    public static RmiClient startClientRMI(boolean isCli, String hostServer, int port) throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry(hostServer, port);

        VirtualServer server = (VirtualServer) registry.lookup("VirtualServer");

        RmiClient client = new RmiClient(server);

        client.run(isCli);

        return client;
    }

    private void connectToGame(String path) {
        try {
            virtualGameStub = (VirtualStub) registry.lookup(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendMessage(MessageS2C message) {
        GameManagerClient.getInstance().addMessageToQueue(message);
    }

    // empty method
    @Override
    public void attachGameStub(VirtualStub gameStub) throws RemoteException {
        virtualGameStub = gameStub;
        System.out.println("Attached Game Stub To Client RMI|");
    }


    @Override
    public void closeConnection() {}

    @Override
    public void sendMessageToServer(MessageC2S message) {
        try{
            if (!serverDown){
                if (message.getType().equals(MessageTypeC2S.CREATE_GAME) ||
                        message.getType().equals(MessageTypeC2S.JOIN_GAME) ||
                        message.getType().equals(MessageTypeC2S.RECONNECT) ||
                        message.getType().equals(MessageTypeC2S.JOINABLE_GAMES)) {
                    if (GameManagerClient.getInstance().canICreateOrJoinAGame()) {
                        message.attachVirtualView(this);
                        server.sendMessage(message);
                    }
                } else {
                    if (virtualGameStub == null) {
                        System.out.println("Looks like you're not in a game!");
                    }

                    virtualGameStub.sendMessage(message);
                }
            }
        }catch (RemoteException e){
            // TODO: should we handle this?
            // TODO: maybe log error with snackbar
            // TODO: server crashed, try reconnecting....
            System.err.println("Remote exception while sending message to server in RMI?");
            throw  new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
