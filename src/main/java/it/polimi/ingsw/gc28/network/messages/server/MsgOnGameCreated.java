package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.View.GameManagerClient;

public class MsgOnGameCreated extends MessageS2C{

    String gameId;
    String playerName;
    int playersLeftToJoin;

    public MsgOnGameCreated(String gameId, String playerName, int playersLeftToJoin){
        this.gameId = gameId;
        this.playerName = playerName;
        this.playersLeftToJoin = playersLeftToJoin;
    }

    @Override
    public void update(GameManagerClient gameManagerClient) {
        gameManagerClient.setGameId(gameId);

        gameManagerClient.setPlayerName(playerName);

        String text = String.format("""
                Welcome %s, may the power be with you!!
                The game has been created successfully with id: %s!
                
                Waiting other %d to join...
                """, playerName, gameId, playersLeftToJoin) ;

        gameManagerClient.writeInConsole(text);
    }
}
