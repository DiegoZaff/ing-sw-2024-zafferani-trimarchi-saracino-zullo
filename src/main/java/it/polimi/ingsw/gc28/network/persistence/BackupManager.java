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

    private final boolean needsSaving;
    private final Game game;

    public BackupManager(Game game, boolean needsSaving){
        this.needsSaving = needsSaving;
        this.game = game;
    }

    public BackupManager(Game game){
        this(game, true);
    }

    public void run(){

        if(game == null){
            return;
        }
        String gameId = this.game.getGameId();
        String directoryPath = "backups/";
        Path path = Paths.get(directoryPath);
        String filePath = directoryPath + gameId + ".backup";

        if(needsSaving){
            try {
                if (!Files.exists(path)) {
                    Files.createDirectories(path);
                }

                try (FileOutputStream fileOut = new FileOutputStream(filePath);
                     ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
                    out.writeObject(game);
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }else{

            Path backupPath = Paths.get(filePath);
            if (Files.exists(backupPath)) {
                try {
                    Files.delete(backupPath);
                } catch (IOException e) {
                    System.out.println("Could not delete file.");
                }
                System.out.println("Backup deleted successfully.");
            } else {
                System.out.println("No backup found to delete.");
            }

        }

    }
}
