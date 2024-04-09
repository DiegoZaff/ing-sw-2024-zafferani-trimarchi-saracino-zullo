package it.polimi.ingsw.gc28.network.rmi;

import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.network.messages.client.MessageC2S;
import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * These are called from the server, and will act on the client.
 */
public interface VirtualView extends Remote {
     void handleMessage(MessageS2C message) throws RemoteException;
//     void showTable(Player player) throws RemoteException;
//
//     void showHand(Player player) throws RemoteException;
//
//     void showPlayerOfTurn() throws RemoteException;
//
//     void showExpectedAction() throws RemoteException;
//
//     void reportError(String details) throws RemoteException;
//
//     void reportMessage(String details) throws RemoteException;
//
//     String getClientID() throws RemoteException;
}
