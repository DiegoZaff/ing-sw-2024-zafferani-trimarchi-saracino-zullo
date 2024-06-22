package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.GameRepresentation;
import it.polimi.ingsw.gc28.view.utils.InformationType;
import it.polimi.ingsw.gc28.view.utils.SnackBarMessage;
/**
 * message sent from the server to the client to notify that the game has restarted
 */
public class MsgOnGameRestarted extends MessageS2C{

    private final GameRepresentation gameRepresentation;
    public MsgOnGameRestarted(GameRepresentation gameRepresentation) {
        super(MessageTypeS2C.GAME_RESTARTED);
        this.gameRepresentation = gameRepresentation;
    }

    @Override
    public void update(GameManagerClient gameManagerClient, boolean isCli) {
        gameManagerClient.setCurrentRepresentation(gameRepresentation);

        if(isCli) {
            String playerOfTurn = gameRepresentation.getPlayerToPlay();

            ActionType actionType = gameRepresentation.getActionExpected();

            String text = String.format("""
                    The game has RE-started! Let's continue where we left :)
                                    
                    It's %s's Turn.
                    Action Expected: %s.
                    """, playerOfTurn, actionType);

            gameManagerClient.writeInConsole(text);
        } else {
            SnackBarMessage msg = new SnackBarMessage("Game restarting...", InformationType.GAME_INFO);
            gameManagerClient.updateSnackBarListener(msg);
            GameManagerClient.getInstance().updateListeners();
        }
    }
}
