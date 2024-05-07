package it.polimi.ingsw.gc28.network.rmi;
import java.util.*;

import it.polimi.ingsw.gc28.View.GameManagerClient;
import it.polimi.ingsw.gc28.View.GameRepresentation;
import it.polimi.ingsw.gc28.View.MessageToServer;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.network.messages.client.*;
import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class RmiClient extends UnicastRemoteObject implements VirtualView {
    final VirtualServer server;

    private String gameId = null;

    private String userName= null;

    final String id;

    MessageToServer messageToServer = MessageToServer.getInstance();

    protected RmiClient(VirtualServer server) throws RemoteException {
        this.server = server;
        this.id = UUID.randomUUID().toString();
    }

    private void run() throws RemoteException {
        runCli();
    }

    private void runCli() throws RemoteException {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String line = scan.nextLine();

            String[] commands = line.split(" ");
            ArrayList<String> commandsList = new ArrayList<>(Arrays.asList(commands));

            if (commandsList.size() < 2) {
                System.out.println("Give me a valid command plz.");
                continue;
            }

            String action = commandsList.getFirst();

            if(action.equals("show")){



            }else{
                Optional<MessageC2S> message;
                message = messageToServer.createMessage(commandsList, this, gameId, userName);

                if (message.isPresent()) {
                    MessageC2S messageToSend = message.get();
                    server.sendMessage(messageToSend);
                }
            }
        }
    }

    public static void startClientRMI(String host, int port) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(host, port);
        VirtualServer server = (VirtualServer) registry.lookup("VirtualServer");

        new RmiClient(server).run();
    }

    @Override
    public void sendMessage(MessageS2C message) {
        GameManagerClient.getInstance().addMessageToQueue(message);
    }


}
