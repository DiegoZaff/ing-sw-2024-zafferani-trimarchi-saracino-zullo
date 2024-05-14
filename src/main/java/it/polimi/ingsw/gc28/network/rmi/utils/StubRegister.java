package it.polimi.ingsw.gc28.network.rmi.utils;

import it.polimi.ingsw.gc28.network.rmi.GameStub;
import it.polimi.ingsw.gc28.network.rmi.VirtualServer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class StubRegister {
    private static Registry registry;

    static String host;

    /**
     * This must be called before first stub registration.
     */
    public static void init(int portRMI, String hostX) throws RemoteException {
        if(registry == null){
            if(hostX != null){
                // this is because sometimes when client connecting from a device different than the server's device
                // client looks up for localhost after it receives the stub:
                // see https://stackoverflow.com/questions/15685686/java-rmi-connectexception-connection-refused-to-host-127-0-1-1
                System.setProperty("java.rmi.server.hostname", hostX);
            }

            registry = LocateRegistry.createRegistry(portRMI);
        }
    }


    public static void register(VirtualServer obj, String name) throws RemoteException {
        if(registry == null){
            throw new RuntimeException();
        }
        registry.rebind(name, obj);
    }
}
