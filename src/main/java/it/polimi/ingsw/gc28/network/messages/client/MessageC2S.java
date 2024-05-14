package it.polimi.ingsw.gc28.network.messages.client;

import it.polimi.ingsw.gc28.controller.GameController;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Optional;

/**
 * Message sent from Client To Server
 */
public abstract class MessageC2S implements Serializable {
    private final MessageTypeC2S type;

    private final String gameId;

    protected MessageC2S(MessageTypeC2S messageType, String gameId) {
        this.type = messageType;
        this.gameId = gameId;
    }

    public Optional<String> getGameId() {
        return Optional.ofNullable(gameId);
    }

    public abstract void execute(GameController controller) throws RemoteException;

    public MessageTypeC2S getType() {
        return type;
    }
}
