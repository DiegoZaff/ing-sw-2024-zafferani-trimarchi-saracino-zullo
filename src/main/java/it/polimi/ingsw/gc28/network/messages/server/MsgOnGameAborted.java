package it.polimi.ingsw.gc28.network.messages.server;
import it.polimi.ingsw.gc28.view.GameManagerClient;
/**
 * message sent from the server to the client to notify that the game was abandoned
 */
public class MsgOnGameAborted extends MessageS2C {
    private final String playerWhoQuit;
    protected MsgOnGameAborted(String playerWhoQuit) {
        super(MessageTypeS2C.GAME_ABORTED);
        this.playerWhoQuit = playerWhoQuit;
    }

    @Override
    public void update(GameManagerClient gameManagerClient, boolean isCli) {
        gameManagerClient.getCurrentRepresentation().setGameAborted(true);

        if(isCli){
            String text = String.format("%s quit the game! He really doesn't want to lose huh?", playerWhoQuit);

            gameManagerClient.writeInConsole(text);
        }else{
            gameManagerClient.updateListeners();
        }
    }
}
