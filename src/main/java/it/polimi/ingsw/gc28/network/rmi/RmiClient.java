package it.polimi.ingsw.gc28.network.rmi;
import java.rmi.registry.LocateRegistry;
import java.util.*;

import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.MessageUtils;
import it.polimi.ingsw.gc28.network.messages.client.*;
import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;
import it.polimi.ingsw.gc28.view.gui.GuiCallable;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class RmiClient extends UnicastRemoteObject implements VirtualView, GuiCallable {
    final VirtualServer server;

    VirtualStub virtualGameStub;

    static Registry registry;

    final String id;



    protected RmiClient(VirtualServer server) throws RemoteException {
        this.server = server;
        this.id = UUID.randomUUID().toString();
    }

    private void run(boolean isCli) throws RemoteException {
        if(isCli){
            runCli();
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

            if (complete) {
                continue;
            }

            Optional<MessageC2S> message;

            String gameId = GameManagerClient.getInstance().getGameId();
            String userName = GameManagerClient.getInstance().getPlayerName();

            message = MessageUtils.createMessage(commandsList, this, gameId, userName);

            if (message.isPresent()) {
                MessageC2S messageToSend = message.get();

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
        System.out.println("Attached Game Stub To Client RMI");
    }


    @Override
    public void closeConnection() {}

    @Override
    public void sendMessageToServer(MessageC2S message) {
        try{
            if (message.getType().equals(MessageTypeC2S.CREATE_GAME) || message.getType().equals(MessageTypeC2S.JOIN_GAME)) {
                if (GameManagerClient.getInstance().canICreateOrJoinAGame()) {
                    server.sendMessage(message);
                }
            } else {
                if (virtualGameStub == null) {
                    System.out.println("Looks like you're not in a game!");
                }

                virtualGameStub.sendMessage(message);
            }
        }catch (RemoteException e){
            // TODO: should we handle this?
            // TODO: maybe log error with snackbar
            System.err.println("Remote exception while sending message to server in RMI?");
            throw  new RuntimeException(e);
        }

    }
}
