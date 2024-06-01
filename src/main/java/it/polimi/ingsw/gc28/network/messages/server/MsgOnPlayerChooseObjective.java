package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.GameRepresentation;
import it.polimi.ingsw.gc28.view.utils.InformationType;
import it.polimi.ingsw.gc28.view.utils.SnackBarMessage;

public class MsgOnPlayerChooseObjective extends MessageS2C{
    private final String playerName;

    private final GameRepresentation gameRepresentation;

    public MsgOnPlayerChooseObjective(String playerName, GameRepresentation gameRepresentation){
        super(MessageTypeS2C.CHOOSE_OBJ);
        this.playerName = playerName;
        this.gameRepresentation = gameRepresentation;
    }

    @Override
    public void update(GameManagerClient gameManagerClient, boolean isCli)  {
        gameManagerClient.setCurrentRepresentation(gameRepresentation);

        String text;

        if(playerName.equals(gameManagerClient.getPlayerName())){

            String personalObjId;
            try {
                personalObjId = gameRepresentation.getRepresentations().get(playerName).getPrivateObjective().getId();
            }catch (Exception e){
                System.err.println("Something is wrong in the private representation!");
                throw new RuntimeException(e);
            }

            text = String.format("""
                You have chosen your objective: %s
                Don't tell anyone!
                """, personalObjId);

        }else{
            text = String.format("""
                %s has chosen his super secret objective!
                """, playerName);
        }

        String nextPlayer = gameManagerClient.getCurrentRepresentation().getPlayerToPlay();
        String playerName = gameManagerClient.getPlayerName();
        ActionType actionType = gameManagerClient.getCurrentRepresentation().getActionExpected();
        if(isCli){
            gameManagerClient.writeInConsole(text);
            gameManagerClient.showPlayerAndAction();

            if(nextPlayer.equals(playerName) && actionType.equals(ActionType.CHOOSE_OBJ)){
                gameManagerClient.showObjectivesToChoose();
            } else if (nextPlayer.equals(playerName) && actionType.equals(ActionType.PLAY_CARD)) {
                gameManagerClient.showHand(true);
                gameManagerClient.showTable();
            }
        }else{
            SnackBarMessage msg = new SnackBarMessage(text, InformationType.GAME_INFO);
            gameManagerClient.updateSnackBarListener(msg);
        }

    }
}

