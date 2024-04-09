package it.polimi.ingsw.gc28.network;

import it.polimi.ingsw.gc28.network.rmi.RmiClient;
import it.polimi.ingsw.gc28.network.socket.ClientTCP;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;

/**
 * @example arguments
 * 127.0.0.1 1234 for TCP
 * 127.0.0.1 1235 for RMI
 */
public class ClientApplication {
    public static void main(String[] args) {
        // for now, it's always CLI

        String host = args[0];
        int port;
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            return;
        }

        // defaults to using sockets
        boolean isRmi = false;

        for (String arg : Arrays.stream(args).skip(2).toList()) {
            if (arg.equals("--rmi")) {
                isRmi = true;
                break;
            }
        }

        if (isRmi) {
            System.out.println("Starting RMI connection...");
            try {
                RmiClient.startClientRMI(host, port);
            } catch (RemoteException | NotBoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Starting TCP connection...");
            try {
                ClientTCP.startClientSocket(host, port);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
