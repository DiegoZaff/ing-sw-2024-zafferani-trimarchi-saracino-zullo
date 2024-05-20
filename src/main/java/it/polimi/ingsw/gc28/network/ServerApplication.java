package it.polimi.ingsw.gc28.network;

import it.polimi.ingsw.gc28.network.rmi.RmiServer;
import it.polimi.ingsw.gc28.network.socket.ServerTCP;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.RemoteException;

/**
 * @example arguments.
 * args:  8887 (port tcp)  8886 (port RMI)
 */
public class ServerApplication {
    public static void main(String[] args) throws IOException {
        int portTCP; // 8886
        int portRMI; // 8887
        String host = null;
        try{
            portTCP = Integer.parseInt(args[0]);
            portRMI = Integer.parseInt(args[1]);
            if(args.length > 2){
                host = args[2];
            }

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
            RmiServer.registerServerRMI(portRMI, host);
        }catch (RemoteException e){
            System.out.println(e.getMessage());
        }

    }
}
