package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.view.GameManagerClient;

public class MsgOnSomeoneElseJoined extends MessageS2C{

    private final String gameId;

    public String getGameId() {
        return gameId;
    }

    private final String playerName;

    public String getPlayerName(){
        return this.playerName;
    }
    private final int playersLeftToJoin;


    public int getPlayersLeftToJoin() {
        return playersLeftToJoin;
    }
    public MsgOnSomeoneElseJoined(String gameId, String playerName, int playersLeftToJoin) {
        super(MessageTypeS2C.SOMEONE_ELSE_JOINED);
        this.gameId = gameId;
        this.playerName = playerName;
        this.playersLeftToJoin = playersLeftToJoin;
    }

    @Override
    public void update(GameManagerClient gameManagerClient, boolean isCli) {
        if(isCli) {

            String text = String.format("""
                    %s has joined the game!! Say hello to him!
                    """, playerName);

            if (playersLeftToJoin > 0) {
                text += String.format("Waiting for other %d players to join...", playersLeftToJoin);
            }

            gameManagerClient.writeInConsole(text);
        }
        else {
            GameManagerClient.getInstance().updateListeners(this);
        }
    }

}
