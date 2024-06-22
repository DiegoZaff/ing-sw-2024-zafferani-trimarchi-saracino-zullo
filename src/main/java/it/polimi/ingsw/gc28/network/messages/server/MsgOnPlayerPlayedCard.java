package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.GameRepresentation;
import it.polimi.ingsw.gc28.view.utils.InformationType;
import it.polimi.ingsw.gc28.view.utils.SnackBarMessage;
/**
 * message sent from the server to the client to notify that a player ha splayed a card
 */
public class MsgOnPlayerPlayedCard extends MessageS2C{

    private final GameRepresentation gameRepresentation;

    private final String cardPlayedId;

    private final  String playerWhoPlayed;

    public MsgOnPlayerPlayedCard(GameRepresentation gameRep, String cardPlayedId, String playerWhoPlayed){
        super(MessageTypeS2C.PLAY_CARD);
        this.gameRepresentation = gameRep;
        this.cardPlayedId = cardPlayedId;
        this.playerWhoPlayed = playerWhoPlayed;
    }

    @Override
    public void update(GameManagerClient gameManagerClient, boolean isCli)  {
        gameManagerClient.setCurrentRepresentation(gameRepresentation);

        String text;

        if(playerWhoPlayed.equals(gameManagerClient.getPlayerName())){

            text = String.format("""
                You have played the card %s
                """, cardPlayedId);

        }else{

            text = String.format("""
                %s has played the card %s
                """, playerWhoPlayed, cardPlayedId);
        }


        if(isCli){
            gameManagerClient.writeInConsole(text);

            String me = gameManagerClient.getPlayerName();

            ActionType nextAction = gameManagerClient.getCurrentRepresentation().getActionExpected();

            String nextPlayer = gameManagerClient.getCurrentRepresentation().getPlayerToPlay();

            gameManagerClient.showPlayerAndAction();

            if(playerWhoPlayed.equals(me) && nextAction.equals(ActionType.DRAW_CARD)){
                gameManagerClient.showDrawableCards();

            }else if(nextPlayer.equals(me) && nextAction.equals(ActionType.CHOOSE_OBJ)){
                gameManagerClient.showObjectivesToChoose();
            }else if(nextPlayer.equals(me) && nextAction.equals(ActionType.PLAY_INITIAL_CARD)){
                gameManagerClient.showCardInitial();
            }
        }else{
            SnackBarMessage msg = new SnackBarMessage(text.replace("\n", ""), InformationType.GAME_INFO);
            gameManagerClient.updateSnackBarListener(msg);
            gameManagerClient.updateListeners(this);
        }
    }


    public String getCardPlayedId() {
        return cardPlayedId;
    }

    public String getPlayerWhoPlayed(){
        return playerWhoPlayed;
    }
}
