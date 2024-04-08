package it.polimi.ingsw.gc28.rmi;

import it.polimi.ingsw.gc28.model.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualView extends Remote {
     void showTable(Player player) throws RemoteException;

     void showHand(Player player) throws RemoteException;

     void showPlayerOfTurn() throws RemoteException;

     void showExpectedAction() throws RemoteException;

     void reportError(String details) throws RemoteException;

     void reportMessage(String details) throws RemoteException;

     String getClientID() throws RemoteException;
}
