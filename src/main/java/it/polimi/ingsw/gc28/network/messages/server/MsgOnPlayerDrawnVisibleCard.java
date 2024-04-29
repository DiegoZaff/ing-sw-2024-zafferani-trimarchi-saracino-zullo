package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.View.GameRepresentation;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;

import java.io.IOException;

public class MsgOnPlayerDrawnVisibleCard extends MessageS2C{

    String playerName;
    String cardId;
    boolean fromGoldDeck;

    public MsgOnPlayerDrawnVisibleCard(String playerName, String cardId, boolean fromGoldDeck){
        this.playerName = playerName;
        this.cardId = cardId;
        this.fromGoldDeck = fromGoldDeck;
    }

    @Override
    public void update(GameRepresentation gameRepresentation) throws IOException {

    }
}
//ora che aggiorniamo tutta la game representation non serve più