package it.polimi.ingsw.gc28.network.messages.client;

import it.polimi.ingsw.gc28.controller.GameController;

import java.rmi.RemoteException;
/**
 * message sent from the client to the server to choose an objective
 */
public class MsgChooseObjective extends MessageC2S {
    private final String playerName;
    private final String cardId;
    public MsgChooseObjective(String playerName, String cardId) {
        super(MessageTypeC2S.CHOOSE_OBJ);
        this.playerName = playerName;
        this.cardId = cardId;
    }

    @Override
    public void execute(GameController controller) throws RemoteException {
        controller.chooseObjectivePersonal(playerName, cardId);
    }
}
