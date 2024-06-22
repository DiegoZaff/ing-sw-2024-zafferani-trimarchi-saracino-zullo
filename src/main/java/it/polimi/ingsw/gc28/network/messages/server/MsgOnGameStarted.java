package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.GameRepresentation;
import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.view.utils.InformationType;
import it.polimi.ingsw.gc28.view.utils.SnackBarMessage;

/**
 * message sent from the server to the client to notify that the game has started
 */
public class MsgOnGameStarted extends MessageS2C{

    GameRepresentation gameRepresentation;

    public MsgOnGameStarted(GameRepresentation gameRepresentation){
        super(MessageTypeS2C.GAME_STARTED);
        this.gameRepresentation = gameRepresentation;
    }

    @Override
    public void update(GameManagerClient gameManagerClient, boolean isCli)  {
        gameManagerClient.setCurrentRepresentation(gameRepresentation);

        String playerOfTurn = gameRepresentation.getPlayerToPlay();

        ActionType actionType = gameRepresentation.getActionExpected();

        if(isCli){
            String text =String.format("""
                The game has started! All players have joined!
                
                It's %s's Turn.
                Action Expected: %s.
                """,playerOfTurn, actionType);

            gameManagerClient.writeInConsole(text);
        }else{
            SnackBarMessage msg = new SnackBarMessage("The game has started! Good Luck!", InformationType.GAME_INFO);
            gameManagerClient.updateSnackBarListener(msg);
            gameManagerClient.updateListeners(this);
        }


        /*
        if(playerOfTurn.equals(gameManagerClient.getPlayerName())){
            gameManagerClient.showCardInitial();
        }
         */
    }

    public GameRepresentation getGameRepresentation() {
        return gameRepresentation;
    }
}
