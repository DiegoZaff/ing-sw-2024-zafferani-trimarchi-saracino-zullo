package it.polimi.ingsw.gc28.network.rmi;
import java.util.*;

import it.polimi.ingsw.gc28.controller.GameController;
import it.polimi.ingsw.gc28.controller.GamesManager;
import it.polimi.ingsw.gc28.gui.GuiApplication;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.network.messages.server.MessageTypeS2C;
import it.polimi.ingsw.gc28.network.messages.server.MsgOnGameCreated;
import it.polimi.ingsw.gc28.network.messages.server.MsgOnGameJoined;
import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.MessageToServer;
import it.polimi.ingsw.gc28.network.messages.client.*;
import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;
import javafx.application.Application;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class RmiClient extends UnicastRemoteObject implements VirtualView {
    final VirtualServer server;

    VirtualStub virtualGameStub;

    private Registry registry;

    final String id;

    MessageToServer messageToServer = MessageToServer.getInstance();

    protected RmiClient(VirtualServer server, Registry registry) throws RemoteException {
        this.registry = registry;
        this.server = server;
        this.id = UUID.randomUUID().toString();
    }

    private void run(boolean isCli) throws RemoteException {
        if(isCli){
            runCli();
        }else{
            runGui();
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
            }else if(action.equals("showPrivateChat")){
                String player = commandsList.get(1);
                if(player.isEmpty()){
                    System.out.println("insert a valid name");
                } else {
                    GameManagerClient.getInstance().showPrivateChat(player);
                }
            }else if (action.equals("?")){
                System.out.println("command List:\n" +
                        "-showCardInitial: print your initial card  \n" +
                        "-showHand: print your hand, add 'down' to the command to print the cards face down\n" +
                        "-showTable: print your table, add a player nickname to print his table\n" +
                        "-showPoints: print the players points\n" +
                        "-showPlayerAndAction: print the next expected move and who is due do play\n" +
                        "-showDrawableCards: print the current drawable card from all the decks\n" +
                        "-showGlobalObjectives: print the global objectives\n" +
                        "-showObjective: print your private objective\n" +
                        "-showObjectivesToChoose: print the objectives that you can choose\n" +
                        "-showChat: show the global chat\n " +
                        "-joinGame gameId myNickname: join the game that has the selected gameId\n " +
                        "-createGame myNickname numberOfPlayers: create a new game\n " +
                        "-chooseObj OBJ_id: choose the selected objective\n " +
                        "-drawCard: draw a card, add goldDeck/resourceDeck to draw a random card from the selected deck or the cardId to draw the selected card\n" +
                        "-playCard cardId up/down x y: play the card at the specified coordinate\n" +
                        "-chat, da implementare");
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

                    if(messageToSend.getType().equals(MessageTypeC2S.CREATE_GAME) || messageToSend.getType().equals(MessageTypeC2S.JOIN_GAME)){
                        if(GameManagerClient.getInstance().canICreateOrJoinAGame()){
                            server.sendMessage(messageToSend);
                        }
                    }else{
                        if(virtualGameStub == null){
                            System.out.println("Looks like you're not in a game!");
                        }

                        virtualGameStub.sendMessage(messageToSend);
                    }
                }
            }
        }
    }

    private void runGui(){
        Application.launch(GuiApplication.class);
    }

    public static void startClientRMI(boolean isCli, Registry registry) throws RemoteException, NotBoundException {
        VirtualServer server = (VirtualServer) registry.lookup("VirtualServer");

        new RmiClient(server, registry).run(isCli);
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
        //connect to game stub
//        if (message.getType().equals(MessageTypeS2C.GAME_CREATED)){
//            MsgOnGameCreated msg = (MsgOnGameCreated) message;
//            String path = String.format("game/%s", msg.getGameId());
//
//            connectToGame(path);
//        }else if(message.getType().equals(MessageTypeS2C.GAME_JOINED)){
//            MsgOnGameJoined msg = (MsgOnGameJoined) message;
//            String path = String.format("game/%s", msg.getGameId());
//
//            connectToGame(path);
//        }

        GameManagerClient.getInstance().addMessageToQueue(message);
    }

    // empty method
    @Override
    public void attachGameStub(VirtualStub gameStub) throws RemoteException {
        virtualGameStub = gameStub;
        System.out.println("Attached Game Stub To Client RMI");
    }
}
