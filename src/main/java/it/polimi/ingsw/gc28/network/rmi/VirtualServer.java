package it.polimi.ingsw.gc28.network.rmi;

import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.cards.CardResource;
import it.polimi.ingsw.gc28.network.messages.client.MessageC2S;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This is called from the client and will act on the server.
 */
public interface VirtualServer extends Remote {
     void sendMessage(MessageC2S message) throws RemoteException;

//     String createGame(VirtualView client, String playerName, int numberOfPlayers) throws RemoteException;

 //    void joinGame(VirtualView client, String playerName, String id) throws  RemoteException;
}
