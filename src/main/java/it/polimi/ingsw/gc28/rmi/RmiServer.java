package it.polimi.ingsw.gc28.rmi;

import it.polimi.ingsw.gc28.controller.GameController;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.cards.CardObjective;
import it.polimi.ingsw.gc28.model.cards.CardResource;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class RmiServer implements VirtualServer {
    final GameController controller;
    final Map<String, VirtualView> clientsConnected = new HashMap<>();

    /**
     * These are client that have set a name and are ready to play.
     */
    final Map<String, String> clientsIDtoName = new HashMap<>();

    public RmiServer(GameController controller) {
        this.controller = controller;
    }

    @Override
    public void connect(VirtualView client) throws RemoteException {
        synchronized (this.clientsConnected){
            System.err.println("new client connected");
            this.clientsConnected.put(client.getClientID(), client);
            client.reportMessage("Choose your name by typing `setName [name]`: ");
        }
    }

    @Override
    public void addPlayerToGame(String clientId, String name) throws RemoteException {
        synchronized (this.clientsConnected){

            if (!this.clientsConnected.containsKey(clientId)){
                System.err.println("Request received from unknown client. Terminating connection");
                return;
            }

            try{
                controller.addPlayerToGame(name);

                clientsIDtoName.put(clientId, name);

                if(controller.hasGameStarted()){
                    informAllClients("""
                            Game has started, choose your personal objective.
                            Type "chooseObj 1" to choose the first card or "chooseObj 2" for the second.
                            """);
                    informAllClientsOfPersonalObjectives();
                }
            }catch (RuntimeException e){
                Optional<VirtualView> client = getClientOfId(clientId);

                if(client.isPresent()){
                    client.get().reportError(e.getMessage());
                }
            }
        }
    }


    private Optional<VirtualView> getClientOfId(String id){
        synchronized (this.clientsConnected){
            return Optional.ofNullable(clientsConnected.get(id));
        }
    }

    private Optional<String> getNameFromId(String id){
        synchronized (this.clientsIDtoName){
            return Optional.ofNullable(clientsIDtoName.get(id));
        }
    }


    private void informAllClients(String message) throws RemoteException{
        synchronized (this.clientsConnected){
            for(VirtualView client : clientsConnected.values()){
                client.reportMessage(message);
            }
        }
    }

    private void informAllClientsOfPersonalObjectives() throws RemoteException{
        synchronized (this.clientsConnected){
            for (Map.Entry<String, VirtualView> entry : this.clientsConnected.entrySet()) {
                String id = entry.getKey();

                Optional<String> name = Optional.ofNullable(clientsIDtoName.get(id));

                // if client is not playing, therefore has not set a name.
                if(name.isEmpty()){
                    continue;
                }

                VirtualView client = entry.getValue();

                ArrayList<CardObjective> personalObjectives = this.controller.getPersonalObjectives(name.get());

                if(personalObjectives.size() != 2){
                    client.reportError("Bad State");
                    return;
                }

                // TODO : implement toString inside Cards
                client.reportMessage(String.format("""
                    First Objective: %s
                    Second Objective %s
                    """, personalObjectives.get(0), personalObjectives.get(1)));
            }
        }
    }



    @Override
    public void playGameCard(Player playingPlayer, CardGame playedCard, boolean isFront, Coordinate coordinates) throws RemoteException {

    }

    @Override
    public void drawGameCard(Player playingPlayer, boolean fromGoldDeck) throws RemoteException {

    }

    @Override
    public void drawGameCard(Player playingPlayer, CardResource CardToDraw, boolean fromVisibleGold) throws RemoteException {

    }

    @Override
    public void chooseObjective(String clientID, int n) throws RemoteException {
        Optional<VirtualView> client = getClientOfId(clientID);
        Optional<String> name = getNameFromId(clientID);

        if(client.isEmpty() || name.isEmpty()){
            return;
        }

        try{
            controller.chooseObjectivePersonal(name.get(), n);
            client.get().reportMessage("You have chosen your objective! Don't forget it! \uD83D\uDE09");

            ActionType expectedAction = controller.getExpectedAction();

            // all players have chosen their objectives
            if(expectedAction.equals(ActionType.PLAY_INITIAL_CARD)){
                Optional<Player> playerToPlay = controller.getPlayerOfTurn();

                if(playerToPlay.isEmpty()){
                    System.err.println("Bad State");
                    return;
                }

                // TODO : toString for ActionType (?)
                // TODO : or implement toMessage(action)
                informAllClients(String.format("""
                        All players have chosen their objective.
                        Let's start the game! It's %s's turn.
                        Expected action: %s
                        """, playerToPlay.get().getName(), expectedAction));
            }
        }catch (Exception e){
            client.get().reportError(e.getMessage());
        }
    }


    public static void main(String[] args) throws RemoteException {
        String name = "VirtualServer";
        try{
            Game model = new Game(4);

            VirtualServer engine = new RmiServer(new GameController(model));
            VirtualServer stub =
                    (VirtualServer) UnicastRemoteObject.exportObject(engine, 0);
            Registry registry = LocateRegistry.createRegistry(1234);
            registry.rebind(name, stub);
            System.out.println("stub bound");
        }catch (Exception e){
            System.err.println(e.getMessage());
        }


    }
}
