package it.polimi.ingsw.gc28.network.rmi;
import java.util.*;

import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.MessageToServer;
import it.polimi.ingsw.gc28.network.messages.client.*;
import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class RmiClient extends UnicastRemoteObject implements VirtualView {
    final VirtualServer server;

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

            if (commandsList.isEmpty()) {
                System.out.println("Give me a valid command plz.");
                continue;
            }

            String action = commandsList.getFirst();

            if(action.equals("showHand")){
                boolean isFront = true;
                if(commandsList.size() == 2){
                    String orientation = commandsList.get(1);
                    isFront = orientation.equals("up");
                }

                GameManagerClient.getInstance().showHand(isFront);

            }else if(action.equals("showCardInitial")){
                GameManagerClient.getInstance().showCardInitial();
            }else if(action.equals("showTable")){
                if(commandsList.size() == 2){
                    String player = commandsList.get(1);

                    GameManagerClient.getInstance().showTable(player);

                }else{
                    GameManagerClient.getInstance().showTable();
                }
            }else if(action.equals("showPoints")){
                GameManagerClient.getInstance().showPoints();
            }else if(action.equals("showPlayerAndAction")){
                GameManagerClient.getInstance().showPlayerAndAction();
            }else if(action.equals("showDrawableCards")){
                GameManagerClient.getInstance().showDrawableCards();
            }else if(action.equals("showGlobalObjectives")){
                GameManagerClient.getInstance().showGlobalObjectives();
            }else if(action.equals("showObjective")){
                GameManagerClient.getInstance().showYourObjective();
            }else if(action.equals("showObjectivesToChoose")){
                GameManagerClient.getInstance().showObjectivesToChoose();
            }else if(action.equals("showChat")){
                GameManagerClient.getInstance().showGlobalChat();
            }else{
                if (commandsList.size() < 2) {
                    System.out.println("Give me a valid command plz.");
                    continue;
                }

                Optional<MessageC2S> message;

                String gameId = GameManagerClient.getInstance().getGameId();
                String userName = GameManagerClient.getInstance().getPlayerName();

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
