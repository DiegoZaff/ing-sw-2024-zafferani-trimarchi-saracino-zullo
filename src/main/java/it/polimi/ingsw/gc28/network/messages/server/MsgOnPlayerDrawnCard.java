package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.GameRepresentation;


public class MsgOnPlayerDrawnCard extends MessageS2C{
    private final GameRepresentation gameRepresentation;

    private final String playerName;
    private final String cardId;

    private final Boolean fromGoldDeck;

    public MsgOnPlayerDrawnCard(GameRepresentation gameRepresentation, String playerName, String cardId, Boolean fromGoldDeck){
        super(MessageTypeS2C.DRAW_CARD);
        this.gameRepresentation = gameRepresentation;
        this.playerName = playerName;
        this.cardId = cardId;
        this.fromGoldDeck = fromGoldDeck;
    }

    @Override
    public void update(GameManagerClient gameManagerClient, boolean isCli)  {
        gameManagerClient.setCurrentRepresentation(gameRepresentation);

        String text;

        if(fromGoldDeck == null){

            // draw card
            if(playerName.equals(gameManagerClient.getPlayerName())) {
                text = String.format("""
                        You have drawn the card %s from the visible cards!
                        """, cardId);
            }else{
                text = String.format("""
                        %s has drawn the card %s from the visible cards!
                        """, playerName, cardId);
            }
        }else{
            String deck;
            if(fromGoldDeck){
                deck = " gold deck";
            }else{
                deck = " resource deck";
            }

            if(playerName.equals(gameManagerClient.getPlayerName())) {
                text = String.format("""
                        You have drawn the card %s from the""", cardId);
            }else{
                text = String.format("""
                        %s has drawn a card from the visible cards!""", playerName);
            }
            text += deck;
        }
        gameManagerClient.writeInConsole(text);

    }

    public GameRepresentation getRepresentation() {
        return gameRepresentation;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getCardId() {
        return cardId;
    }

    public Boolean getFromGoldDeck() {
        return fromGoldDeck;
    }
}
//ora che aggiorniamo tutta la game representation non serve più