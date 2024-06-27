package it.polimi.ingsw.gc28.network.rmi;


import it.polimi.ingsw.gc28.controller.GameController;
import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;

import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * This is called from the server, and will act on the client.
 */
public interface VirtualView extends Remote {
     void sendMessage(MessageS2C message) throws RemoteException;

     void attachGameStub(VirtualStub gameStub) throws RemoteException;

     void closeConnectionGame() throws RemoteException;
}
