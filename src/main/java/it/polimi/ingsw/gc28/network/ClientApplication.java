package it.polimi.ingsw.gc28.network;

import it.polimi.ingsw.gc28.network.rmi.RmiClient;
import it.polimi.ingsw.gc28.network.socket.ClientTCP;
import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.gui.GuiApplication;
import javafx.application.Application;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;

/**
 * @example arguments
 * 127.0.0.1 1234 for TCP
 * 127.0.0.1 1235 for RMI
 */
public class ClientApplication {
    public static void main(String[] args) {
        // for now, it's always CLI

        if(args.length < 3){
            System.err.println("You need to supply three arguments: [ip-server] [ip-client] [port]");
        }

        String hostServer = args[0];
        String hostClient = args[1];

        int port;
        try {
            port = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            return;
        }

        // defaults to using sockets
        boolean isRmi = false;
        boolean isCli = true;

        for (String arg : Arrays.stream(args).skip(2).toList()) {
            if (arg.equals("--rmi")) {
                isRmi = true;
            }else if(arg.equals("--gui") || arg.equals("-g") || arg.equals("-G")){
                isCli = false;
            }
        }

        GameManagerClient.isCli = isCli;

        if(isCli){
            if (isRmi) {
                System.out.println("Starting RMI connection...");
                try {
                    // TODO : probabilmente si può togliere?
                    System.setProperty("java.rmi.server.hostname", hostClient);

                    RmiClient.startClientRMI(isCli, hostServer, port);
                } catch (RemoteException | NotBoundException e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("Starting TCP connection...");
                try {
                    ClientTCP.startClientSocket(hostServer, port, isCli);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }else{
            Application.launch(GuiApplication.class);
        }
    }
}
