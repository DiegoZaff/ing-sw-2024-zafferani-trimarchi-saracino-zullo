package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.utils.InformationType;
import it.polimi.ingsw.gc28.view.utils.SnackBarMessage;
/**
 * message sent from the server to the client to notify that someone reconnected to the game
 */
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
    public void update(GameManagerClient gameManagerClient, boolean isCli) {
        if(isCli){
            String text = String.format("""
            %s has reconnected to the game! I bet you missed him/her!
            """, playerName);

            if(playersLeftToJoin > 0) {
                text += String.format("Waiting for other %d players to reconnect...", playersLeftToJoin);
            }
            gameManagerClient.writeInConsole(text);

        }else{
            SnackBarMessage msg = new SnackBarMessage(String.format("%s has reconnected to the game!", playerName), InformationType.GAME_INFO);
            gameManagerClient.updateSnackBarListener(msg);
        }


    }
}
