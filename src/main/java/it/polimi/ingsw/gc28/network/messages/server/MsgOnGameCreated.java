package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.utils.InformationType;
import it.polimi.ingsw.gc28.view.utils.SnackBarMessage;

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
    public int getPlayersLeftToJoin() {
        return playersLeftToJoin;
    }

    public MsgOnGameCreated(String gameId, String playerName, int playersLeftToJoin){
        super(MessageTypeS2C.GAME_CREATED);
        this.gameId = gameId;
        this.playerName = playerName;
        this.playersLeftToJoin = playersLeftToJoin;
    }

    @Override
    public void update(GameManagerClient gameManagerClient, boolean isCli) {
        if(isCli) {

            gameManagerClient.setGameId(gameId);

            gameManagerClient.setPlayerName(playerName);

            if(isCli){
                String text = String.format("""
                    Welcome %s, may the power be with you!!
                    The game has been created successfully with id: %s
                                    
                    Waiting other %d to join...
                    """, playerName, gameId, playersLeftToJoin);


                gameManagerClient.writeInConsole(text);
            }


        } else {
            SnackBarMessage msg = new SnackBarMessage("You have successfully created the game!", InformationType.GAME_INFO);
            gameManagerClient.updateSnackBarListener(msg);
            GameManagerClient.getInstance().updateListeners(this);
        }
    }
}
