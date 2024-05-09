package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.GameRepresentation;

public class MsgOnPlayerPlayedCard extends MessageS2C{

    private final GameRepresentation gameRepresentation;

    private final String cardPlayedId;

    private final  String playerWhoPlayed;

    public MsgOnPlayerPlayedCard(GameRepresentation gameRep, String cardPlayedId, String playerWhoPlayed){
        this.gameRepresentation = gameRep;
        this.cardPlayedId = cardPlayedId;
        this.playerWhoPlayed = playerWhoPlayed;
    }

    @Override
    public void update(GameManagerClient gameManagerClient)  {
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

        gameManagerClient.writeInConsole(text);    }


    public String getCardPlayedId() {
        return cardPlayedId;
    }

    public String getPlayerWhoPlayed(){
        return playerWhoPlayed;
    }
}
