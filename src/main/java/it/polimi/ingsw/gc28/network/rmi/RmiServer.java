package it.polimi.ingsw.gc28.network.rmi;

import it.polimi.ingsw.gc28.controller.GamesManager;
import it.polimi.ingsw.gc28.network.messages.client.MessageC2S;
import it.polimi.ingsw.gc28.network.rmi.utils.StubRegister;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiServer  implements VirtualServer {


    public RmiServer(){}


    public static void registerServerRMI(int portRMI, String host) throws RemoteException {
        String name = "VirtualServer";
        try{
            StubRegister.init(portRMI, host);
            VirtualServer engine = new RmiServer();
            VirtualServer stub =
                    (VirtualServer) UnicastRemoteObject.exportObject(engine, 0);
            StubRegister.register(stub, name);
            System.out.println("server RMI stub bound");
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
    }

    public void sendMessage(MessageC2S message) throws RemoteException {
        GamesManager.getInstance().addMessageToQueue(message);
    }

}
