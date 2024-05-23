package it.polimi.ingsw.gc28.network.persistence;


import it.polimi.ingsw.gc28.model.Game;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;



/**
 * This class starts a thread which backups up a game to the filesystem
 */
public class BackupManager extends Thread {

    private final Game game;

    public BackupManager(Game game){
        this.game = game;
    }

    public void run(){

        if(game == null){
            return;
        }
        String gameId = this.game.getGameId();

        String directoryPath = "backups/";
        Path path = Paths.get(directoryPath);

        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            String filePath = directoryPath + gameId + ".backup";
            try (FileOutputStream fileOut = new FileOutputStream(filePath);
                 ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
                out.writeObject(game);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
