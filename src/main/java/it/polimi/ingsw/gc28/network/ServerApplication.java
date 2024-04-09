package it.polimi.ingsw.gc28.network;

import it.polimi.ingsw.gc28.network.rmi.RmiServer;
import it.polimi.ingsw.gc28.network.socket.ServerTCP;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.RemoteException;

/**
 * @example arguments.
 * args:  1234 (port tcp)  1235 (port RMI)
 */
public class ServerApplication {
    public static void main(String[] args) throws IOException {
        int portTCP; // 1234
        int portRMI; // 1235
        try{
            portTCP = Integer.parseInt(args[0]);
            portRMI = Integer.parseInt(args[1]);
        }catch (NumberFormatException e){
            System.out.println(e.getMessage());
            return;
        }

        try{
            System.out.println("Waiting for tcp connections...");
            // start thread
            new ServerTCP(portTCP).start();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

        try{
            System.out.println("Registering RMI server...");
            // this is not a thread. It just registers the stub.
            RmiServer.registerServerRMI(portRMI);
        }catch (RemoteException e){
            System.out.println(e.getMessage());
        }

    }
}
