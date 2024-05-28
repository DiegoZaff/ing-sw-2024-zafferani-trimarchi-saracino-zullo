package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.GameRepresentation;

public class MsgOnChooseColor extends MessageS2C{

    private final String playerName;
    private final GameRepresentation gameRepresentation;

    public MsgOnChooseColor(String playerName, GameRepresentation gameRepresentation){
        super(MessageTypeS2C.CHOOSE_COLOR);
        this.playerName = playerName;
        this.gameRepresentation = gameRepresentation;
    }
    @Override
    public void update(GameManagerClient gameManagerClient) {
        gameManagerClient.setCurrentRepresentation(gameRepresentation);
        String text;

        if(playerName.equals(gameManagerClient.getPlayerName())){

            text = """
                You have chosen your color:
                """;

        }else{
            text = String.format("""
                %s has chosen his color!
                """, playerName);
        }
        gameManagerClient.writeInConsole(text);
        gameManagerClient.showPlayerAndAction();

        String me = gameManagerClient.getPlayerName();

        ActionType nextAction = gameManagerClient.getCurrentRepresentation().getActionExpected();

        String nextPlayer = gameManagerClient.getCurrentRepresentation().getPlayerToPlay();

        if(nextPlayer.equals(me) && nextAction.equals(ActionType.PLAY_INITIAL_CARD)){
            gameManagerClient.showCardInitial();
        }
    }
}
