package it.polimi.ingsw.gc28.rmi;

import it.polimi.ingsw.gc28.model.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualView extends Remote {
    public void showTable(Player player) throws RemoteException;

    public void showHand(Player player) throws RemoteException;

    public void showPlayerOfTurn() throws RemoteException;

    public void showExpectedAction() throws RemoteException;

    public void reportError(String details) throws RemoteException;
}
