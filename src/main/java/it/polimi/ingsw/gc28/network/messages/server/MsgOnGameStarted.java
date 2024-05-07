package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.View.GameManagerClient;
import it.polimi.ingsw.gc28.View.GameRepresentation;
import it.polimi.ingsw.gc28.model.actions.utils.ActionType;



public class MsgOnGameStarted extends MessageS2C{

    GameRepresentation gameRepresentation;

    public MsgOnGameStarted(GameRepresentation gameRepresentation){
        this.gameRepresentation = gameRepresentation;
    }

    @Override
    public void update(GameManagerClient gameManagerClient)  {
        gameManagerClient.setCurrentRepresentation(gameRepresentation);

        String playerOfTurn = gameRepresentation.getPlayerToPlay();

        ActionType actionType = gameRepresentation.getActionExpected();

        String text =String.format("""
                The game has started! All players have joined!
                
                It's %s's Turn.
                Action Expected: %s.
                """,playerOfTurn, actionType);

        gameManagerClient.writeInConsole(text);
    }
}
