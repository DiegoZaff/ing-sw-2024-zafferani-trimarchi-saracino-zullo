package it.polimi.ingsw.gc28.controller;

import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;

import java.util.HashMap;
import java.util.Map;

public class GamesManager {

    private static GamesManager instance;

    // TODO : change this so that it also stores clients connected to the game (sockets and virtual clients (RMI))
    private Map<String, GameController> mapGames;

    private GamesManager() {
        mapGames = new HashMap<>();
    }

    public static GamesManager getInstance() {
        if (instance == null) {
            instance = new GamesManager();
        }
        return instance;
    }

    // TODO : implement this function
    public void executeClientMessage(MessageS2C message){}


}
