package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.view.GameManagerClient;


public class MsgOnGameJoined extends MessageS2C{

    private final String gameId;

    public String getGameId() {
        return gameId;
    }

    String playerName;
    int playersLeftToJoin;

    public MsgOnGameJoined(String gameId, String playerName, int playersLeftToJoin){
        super(MessageTypeS2C.GAME_JOINED);
        this.gameId = gameId;
        this.playerName = playerName;
        this.playersLeftToJoin = playersLeftToJoin;
    }

    @Override
    public void update(GameManagerClient gameManagerClient)  {

        gameManagerClient.setGameId(gameId);

        gameManagerClient.setPlayerName(playerName);



        String text = String.format("""
                Welcome %s, you joined the game of id %s
                """, playerName, gameId);

        if(playersLeftToJoin > 0) {
            text += String.format("Waiting for other %d players to join...", playersLeftToJoin);
        }

        gameManagerClient.writeInConsole(text);

    }
}
