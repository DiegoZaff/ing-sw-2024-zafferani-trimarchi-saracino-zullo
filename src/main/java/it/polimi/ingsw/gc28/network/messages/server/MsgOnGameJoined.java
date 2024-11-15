package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.utils.InformationType;
import it.polimi.ingsw.gc28.view.utils.SnackBarMessage;

/**
 * message sent from the server to the client to notify that the game has been joined
 */
public class MsgOnGameJoined extends MessageS2C{

    private final String gameId;

    public String getGameId() {
        return gameId;
    }

    private final String playerName;

    public String getPlayerName(){
        return this.playerName;
    }
    private final int playersLeftToJoin;

    private final int nPlayers;

    public int getPlayersLeftToJoin() {
        return playersLeftToJoin;
    }

    public MsgOnGameJoined(String gameId, String playerName, int playersLeftToJoin, int nPlayers){
        super(MessageTypeS2C.GAME_JOINED);
        this.gameId = gameId;
        this.playerName = playerName;
        this.playersLeftToJoin = playersLeftToJoin;
        this.nPlayers = nPlayers;
    }

    @Override
    public void update(GameManagerClient gameManagerClient, boolean isCli)  {
        gameManagerClient.setGameId(gameId);
        gameManagerClient.setPlayerName(playerName);
        gameManagerClient.setNPlayers(nPlayers);
        gameManagerClient.setPlayersIn(nPlayers - playersLeftToJoin);
        String text = String.format("Welcome %s", playerName);

        if(playersLeftToJoin > 0) {
            text += String.format("\nWaiting for other %d players to join...", playersLeftToJoin);
        }

        if(isCli){
            gameManagerClient.writeInConsole(text);
        }else{
            SnackBarMessage msg = new SnackBarMessage(text, InformationType.GAME_INFO);
            gameManagerClient.updateSnackBarListener(msg);
            gameManagerClient.updateListeners(this);
        }
    }

    public int getnPlayers() {
        return nPlayers;
    }
}
