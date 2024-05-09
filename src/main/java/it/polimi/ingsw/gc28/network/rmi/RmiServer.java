package it.polimi.ingsw.gc28.network.rmi;

import it.polimi.ingsw.gc28.controller.GamesManager;
import it.polimi.ingsw.gc28.network.messages.client.MessageC2S;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RmiServer  implements VirtualServer  {

    public RmiServer(){}


    public static void registerServerRMI(int portRMI, String host) throws RemoteException {
        String name = "VirtualServer";
        try{
            // this is because sometimes when client connecting from a device different than the server's device
            // client looks up for localhost after it receives the stub:
            // see https://stackoverflow.com/questions/15685686/java-rmi-connectexception-connection-refused-to-host-127-0-1-1
            if(host != null){
                System.setProperty("java.rmi.server.hostname", host);
            }

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

}
