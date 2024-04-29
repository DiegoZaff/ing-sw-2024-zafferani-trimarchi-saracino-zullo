package it.polimi.ingsw.gc28.network.rmi;

import it.polimi.ingsw.gc28.controller.GamesManager;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.network.messages.client.MessageC2S;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RmiServer  implements VirtualServer  {

    public RmiServer(){}


    public static void registerServerRMI(int portRMI) throws RemoteException {
        String name = "VirtualServer";
        try{
            VirtualServer engine = new RmiServer();
            VirtualServer stub =
                    (VirtualServer) UnicastRemoteObject.exportObject(engine, 0);
            Registry registry = LocateRegistry.createRegistry(portRMI);
            registry.rebind(name, stub);
            System.out.println("server RMI stub bound");
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
    }
    public void sendMessage(MessageC2S message) throws RemoteException {
        GamesManager.getInstance().addMessageToQueue(message);
    }


//    @Override
//    public void createGame(VirtualView client, String userName, int numberOfPlayers) throws RemoteException {
//        GamesManager.getInstance().createGame(client, userName, numberOfPlayers);
//    }
//
//    @Override
//    public void joinGame(VirtualView client, String gameId, String userName) throws RemoteException {
//        GamesManager.getInstance().joinGame(client, gameId, userName);
//    }
//
//    @Override
//    public void playGameCard(String playerName, String gameId, String cardId, boolean isFront, Coordinate coordinate) throws RemoteException {
//        GamesManager.getInstance().playGameCard(playerName, gameId, cardId, isFront, coordinate);
//    }
//
//    @Override
//    public void drawGameCard(String playerName, String gameId, boolean fromGoldDeck) throws RemoteException {
//        GamesManager.getInstance().drawCard(playerName, gameId, fromGoldDeck);
//    }
//
//    @Override
//    public void drawGameCard(String playerName, String gameId, String cardId) throws RemoteException {
//        GamesManager.getInstance().drawCard(playerName, gameId, cardId);
//    }
//
//    @Override
//    public void chooseObjective(String playerName, String gameId, String cardId) throws RemoteException {
//        GamesManager.getInstance().chooseObjective(playerName, gameId, cardId);
//
//    }
}
