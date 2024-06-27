package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.utils.InformationType;
import it.polimi.ingsw.gc28.view.utils.SnackBarMessage;
/**
 * message sent from the server to the client to notify that the game was created
 */
public class MsgOnGameCreated extends MessageS2C{

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

    public MsgOnGameCreated(String gameId, String playerName, int playersLeftToJoin, int nPlayers){
        super(MessageTypeS2C.GAME_CREATED);
        this.gameId = gameId;
        this.playerName = playerName;
        this.playersLeftToJoin = playersLeftToJoin;
        this.nPlayers = nPlayers;
    }

    @Override
    public void update(GameManagerClient gameManagerClient, boolean isCli) {
        gameManagerClient.setNPlayers(nPlayers);
        gameManagerClient.setPlayersIn(1);
        gameManagerClient.setGameId(gameId);
        gameManagerClient.setPlayerName(playerName);
        gameManagerClient.setCanBeRecreated();

        if(isCli) {
            String text = String.format("""
                 Welcome %s, may the power be with you!!
                 The game has been created successfully with id: %s
                                    
                 Waiting other %d to join...
                 """, playerName, gameId, playersLeftToJoin);


            gameManagerClient.writeInConsole(text);
        } else {
            SnackBarMessage msg = new SnackBarMessage("You have successfully created the game!", InformationType.GAME_INFO);
//            System.out.println(gameId);
            gameManagerClient.updateSnackBarListener(msg);
            GameManagerClient.getInstance().updateListeners(this);
        }
    }

    public int getNPlayers() {
        return nPlayers;
    }
}
