package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.GameRepresentation;

public class MsgOnPlayerReconnected extends MessageS2C{
    private final String gameId;

    public String getGameId() {
        return gameId;
    }

    private final String playerName;

    private final int playersLeftToReconnect;


    public MsgOnPlayerReconnected(String gameId, String playerName, int playersLeftToReconnect) {
        super(MessageTypeS2C.RECONNECTED);
        this.gameId = gameId;
        this.playerName = playerName;
        this.playersLeftToReconnect = playersLeftToReconnect;
    }

    @Override
    public void update(GameManagerClient gameManagerClient) {
        gameManagerClient.setGameId(gameId);

        gameManagerClient.setPlayerName(playerName);

        String text = String.format("""
                Welcome back %s, you reconnected to the game of id %s
                """, playerName, gameId);

        if(playersLeftToReconnect > 0) {
            text += String.format("Waiting for other %d players to reconnect...", playersLeftToReconnect);
        }

        gameManagerClient.writeInConsole(text);
    }

}
