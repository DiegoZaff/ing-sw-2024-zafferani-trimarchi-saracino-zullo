package it.polimi.ingsw.gc28.network.rmi;
import java.util.UUID;

import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.network.messages.client.MessageC2S;
import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class RmiClient extends UnicastRemoteObject implements VirtualView {
    final VirtualServer server;

    final String id;

    protected RmiClient(VirtualServer server) throws RemoteException {
        this.server = server;
        this.id = UUID.randomUUID().toString();
    }

    private void run() throws RemoteException {
        runCli();
    }

    // TODO: unify this with cli reader inside ClientTCP. The process of reading from CLI is the same
    private void runCli() throws RemoteException {
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

            String action = commandsList.getFirst();

            // TODO : construct this message
            MessageC2S message = null;

            server.handleMessage(message);

        }
    }




    public static void startClientRMI(String host, int port) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(host, port);
        VirtualServer server = (VirtualServer) registry.lookup("VirtualServer");

        new RmiClient(server).run();
    }

    @Override
    public void handleMessage(MessageS2C message) throws RemoteException {
        // TODO : implement handling of message coming from server.
    }


    // TODO : aggiungi metodi che fanno cose sul client. Metodi della virtual View
}
