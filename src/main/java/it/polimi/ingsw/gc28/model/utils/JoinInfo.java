package it.polimi.ingsw.gc28.model.utils;

import java.io.Serializable;
import java.util.ArrayList;

public class JoinInfo implements Serializable {
    private final String gameId;

    private final ArrayList<String> playersAlreadyIn;

    private final Integer nPlayers;

    public JoinInfo(String gameId, ArrayList<String> playersAlreadyIn, Integer nPlayers) {
        this.gameId = gameId;
        this.playersAlreadyIn = playersAlreadyIn;
        this.nPlayers = nPlayers;
    }

    public String getGameId() {
        return gameId;
    }

    public ArrayList<String> getPlayersAlreadyIn() {
        return playersAlreadyIn;
    }

    @Override
    public String toString() {

        String players = String.join(", ", playersAlreadyIn);

        return String.format("Game id: %s --- Players in: %s\n", gameId, players);
    }

    public Integer getnPlayers() {
        return nPlayers;
    }
}
