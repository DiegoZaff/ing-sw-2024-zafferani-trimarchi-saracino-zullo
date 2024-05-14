package it.polimi.ingsw.gc28.network.rmi;

import it.polimi.ingsw.gc28.controller.GameController;
import it.polimi.ingsw.gc28.network.messages.client.MessageC2S;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualStub extends Remote {
    public GameController getController() throws  RemoteException;
    void sendMessage(MessageC2S message) throws RemoteException;


}
