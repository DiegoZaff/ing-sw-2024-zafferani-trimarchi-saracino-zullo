package it.polimi.ingsw.gc28.network.rmi;
import java.util.UUID;

import it.polimi.ingsw.gc28.View.GameManagerClient;
import it.polimi.ingsw.gc28.View.GameRepresentation;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.network.messages.client.*;
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

    private String gameId;

    private String userName;

    final String id;

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

            if(commandsList.size() < 2){
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

                    Integer nPlayers = null;
                    try {
                        nPlayers = Integer.parseInt(commandsList.get(2));
                    } catch (NumberFormatException e) {
                        System.err.println(e.getMessage());
                        return;
                    }

                    MsgCreateGame CGMess = new MsgCreateGame(gameId , userName, nPlayers, this ); //controllo
                    server.sendMessage(CGMess);


                    break;
                case "joinGame":

                    if (commandsList.size() != 3) {
                        System.err.println("Invalid format");
                        return;
                    }

                    String gameIdToJoin = commandsList.get(1);

                    userName = commandsList.get(2);

                    MsgJoinGame JGMess = new MsgJoinGame(this, gameIdToJoin, userName); //controllo
                    server.sendMessage(JGMess);

                    break;
                case "chooseObj": {

                    if (commandsList.size() != 2) {
                        System.err.println("Invalid format");
                        return;
                    }

                    String cardId = commandsList.get(1);

                    MsgChooseObjective COMess = new MsgChooseObjective(userName, gameId, cardId); //controllo
                    server.sendMessage(COMess);

                    break;
                }
                case "drawCard":

                    if (commandsList.size() < 2) {
                        System.err.println("Invalid format");
                        return;
                    }

                    String arg = commandsList.get(1);



                    if (arg.equals("goldDeck")) {
                        MsgDrawGameCard DGCMessGold = new MsgDrawGameCard( userName, gameId, true);
                        server.sendMessage(DGCMessGold);

                    } else if (arg.equals("resourceDeck")) {
                        MsgDrawGameCard DGCMessRes = new MsgDrawGameCard( userName, gameId, false);
                        server.sendMessage(DGCMessRes);

                    } else if (arg.startsWith("RES") || arg.startsWith("GOLD")) {
                        MsgDrawnVisibleGameCard DGCMessFU = new MsgDrawnVisibleGameCard( userName, gameId, arg);
                        server.sendMessage(DGCMessFU);

                    } else {
                        System.err.println("Invalid format");
                    }
                    break;
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
