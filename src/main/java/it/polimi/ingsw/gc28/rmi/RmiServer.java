package it.polimi.ingsw.gc28.rmi;

import it.polimi.ingsw.gc28.controller.GameController;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.cards.CardObjective;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RmiServer implements VirtualServer {
    final GameController controller;
    final List<VirtualView> clients = new ArrayList<>();

    public RmiServer(GameController controller) {
        this.controller = controller;
    }

    @Override
    public void connect(VirtualView client) throws RemoteException {
        synchronized (this.clients){
            System.err.println("new client connected");
            this.clients.add(client);
        }
    }

    @Override
    public void addPlayerToGame(String name) throws RemoteException {

    }

    @Override
    public void playGameCard(Player playingPlayer, CardGame playedCard, boolean isFront, Coordinate coordinates) throws RemoteException {

    }

    @Override
    public void drawGameCard(Player playingPlayer, boolean fromGoldDeck) throws RemoteException {

    }

    @Override
    public void drawGameCard(Player playingPlayer, CardGame CardToDraw) throws RemoteException {

    }

    @Override
    public void chooseObjective(Player player, CardObjective card) throws RemoteException {

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
