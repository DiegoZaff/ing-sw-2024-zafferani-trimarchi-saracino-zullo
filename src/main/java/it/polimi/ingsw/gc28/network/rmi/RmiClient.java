package it.polimi.ingsw.gc28.network.rmi;
import java.util.UUID;

import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.Table;
import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
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

                    String username = commandsList.get(1);

                    Integer nPlayers = null;
                    try {
                        nPlayers = Integer.parseInt(commandsList.get(2));
                    } catch (NumberFormatException e) {
                        System.err.println(e.getMessage());
                        return;
                    }

                    server.createGame(this, username, nPlayers);
                    break;
                case "joinGame":

                    if (commandsList.size() != 3) {
                        System.err.println("Invalid format");
                        return;
                    }

                    String gameId = commandsList.get(1);

                    String userName = commandsList.get(2);

                    server.joinGame(this, gameId, userName);

                    break;
                case "chooseObj": {

                    if (commandsList.size() != 2) {
                        System.err.println("Invalid format");
                        return;
                    }

                    String cardId = commandsList.get(1);

                    server.chooseObjective(this.userName, this.gameId, cardId);
                    break;
                }
                case "drawCard":

                    if (commandsList.size() < 2) {
                        System.err.println("Invalid format");
                        return;
                    }

                    String arg = commandsList.get(1);

                    if (arg.equals("goldDeck")) {
                        server.drawGameCard(this.userName, this.gameId, true);
                    } else if (arg.equals("resourceDeck")) {
                        server.drawGameCard(this.userName, this.gameId, false);
                    } else if (arg.startsWith("RES") || arg.startsWith("GOLD")) {
                        server.drawGameCard(this.userName, this.gameId, arg);
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

                    server.playGameCard(this.userName, cardId, this.gameId, isFront, new Coordinate(x, y));
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
    public void onGameCreated(String gameId, String playerName, int playersLeftToJoin) throws RemoteException {
        // TODO : implement this
        System.out.println("Received GameCreation: " + gameId + " " + playerName + " " + playersLeftToJoin);

    }

    @Override
    public void onGameJoined(String gameId, String playerName, int playersLeftToJoin) throws RemoteException {

    }

    @Override
    public void onGameStarted(ArrayList<Player> players) throws RemoteException {

    }

    @Override
    public void onPlayerPlayedCard(String playerName, Table newTable, int newPlayerPoints) throws RemoteException {

    }

    @Override
    public void onPlayerDrawnCard(String playerName, String cardId, boolean fromGoldDeck) throws RemoteException {

    }

    @Override
    public void onPlayerDrawnCard(String playerName, String cardId) throws RemoteException {

    }

    @Override
    public void onPlayerChoseObjective(String playerName, String cardId) throws RemoteException {

    }

    @Override
    public void reportError(String details) throws RemoteException {

    }

    @Override
    public void reportMessage(String details) throws RemoteException {

    }

    @Override
    public void onNextExpectedPlayerAction(ActionType actionType, String playerOfTurn) throws RemoteException {

    }


}
