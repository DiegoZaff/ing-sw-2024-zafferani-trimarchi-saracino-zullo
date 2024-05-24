package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.view.GameManagerClient;

public class MsgOnSomeoneElseReconnected extends  MessageS2C{
    private final String playerName;

    public String getPlayerName(){
        return this.playerName;
    }
    private final int playersLeftToJoin;

    public MsgOnSomeoneElseReconnected(String playerName, int playersLeftToJoin) {
        super(MessageTypeS2C.SOMEONE_ELSE_RECONNECTED);
        this.playerName = playerName;
        this.playersLeftToJoin = playersLeftToJoin;
    }

    @Override
    public void update(GameManagerClient gameManagerClient) {
        String text = String.format("""
            %s has reconnected to the game! I bet you missed him/her!
            """, playerName);

        if(playersLeftToJoin > 0) {
            text += String.format("Waiting for other %d players to reconnect...", playersLeftToJoin);
        }

        gameManagerClient.writeInConsole(text);
    }
}
