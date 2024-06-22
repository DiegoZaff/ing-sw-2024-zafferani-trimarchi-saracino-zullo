package it.polimi.ingsw.gc28.network.messages.client;

import it.polimi.ingsw.gc28.controller.GameController;

import java.rmi.RemoteException;
/**
 * message sent from the client to the server to draw a card
 */
public class MsgDrawGameCard extends MessageC2S{
    String playerName;
    boolean fromGoldDeck;
    public MsgDrawGameCard(String playerName, boolean fromGoldDeck) {
        super(MessageTypeC2S.DRAW_CARD_DECK);
        this.playerName = playerName;
        this.fromGoldDeck = fromGoldDeck;
    }
    @Override
    public void execute(GameController controller) throws RemoteException {
        controller.drawCard(playerName, fromGoldDeck);
    }
}
