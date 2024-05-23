package it.polimi.ingsw.gc28.network;

import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.network.rmi.RmiServer;
import it.polimi.ingsw.gc28.network.socket.ServerTCP;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * @example arguments.
 * args:  8887 (port tcp)  8886 (port RMI) (serverIP)
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


        // wait to restore games before allowing reconnections
        restoreBackUppedGames();

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

    /**
     * This method restores back-upped games
     */
    public static void restoreBackUppedGames() {

        String directoryName = "backups";

        Path path = Paths.get(directoryName);

        if (!Files.exists(path)) {
            System.out.println("Backup directory does not exist.");
            return;
        }

        ArrayList<Game> games = new ArrayList<>();

        File directory = new File(directoryName);
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".backup"));

        if (files != null) {
            for (File file : files) {
                try (FileInputStream fileIn = new FileInputStream(file);
                     ObjectInputStream in = new ObjectInputStream(fileIn)) {
                    Game game = (Game) in.readObject();
                    games.add(game);
                    System.out.println("Game object deserialized: " + file.getName());
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println(e.getMessage());
                    // delete if file cannot be restored
                    //noinspection ResultOfMethodCallIgnored
                    file.delete();
                }
            }
        } else {
            System.out.println("No backup files found.");
        }

        for (Game game : games) {
            System.out.println("Restored game: " + game);
        }
    }
}
