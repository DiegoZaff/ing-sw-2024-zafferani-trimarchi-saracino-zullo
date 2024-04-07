package it.polimi.ingsw.gc28.rmi;

import it.polimi.ingsw.gc28.model.Player;

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

    protected RmiClient(VirtualServer server) throws RemoteException {
        this.server = server;
    }

    private void run() throws RemoteException {
        this.server.connect(this);
        runCli();
    }

    private void runCli() throws RemoteException {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String line = scan.nextLine();

            String[] commands = line.split(" ");
            ArrayList<String> commandsList = new ArrayList<>(Arrays.asList(commands));

            if(commandsList.size() < 2){
                System.err.println("Give me a valid command plz.");
                return;
            }

            String action = commandsList.getFirst();

            if (action.equals("playCard")) {
                // server.play...
            } else if (action.equals("drawCard")) {
                //server.draw...
            }else if(action.equals("chooseObj")){
                //server.choose...
            }else if(action.equals("setName")){
                //server.addPlayer...
            }
        }
    }


    @Override
    public void showTable(Player player) throws RemoteException {

    }

    @Override
    public void showHand(Player player) throws RemoteException {

    }

    @Override
    public void showPlayerOfTurn() throws RemoteException {

    }

    @Override
    public void showExpectedAction() throws RemoteException {

    }

    @Override
    public void reportError(String details) throws RemoteException {
        // TODO Attenzione! Questo può causare data race con il thread dell'interfaccia o un altro thread
        System.err.print("\n[ERROR] " + details + "\n> ");
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(args[0], 1234);
        VirtualServer server = (VirtualServer) registry.lookup("VirtualServer");

        new RmiClient(server).run();
    }
}