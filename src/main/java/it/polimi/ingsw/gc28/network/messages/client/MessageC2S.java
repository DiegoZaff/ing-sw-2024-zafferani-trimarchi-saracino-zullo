package it.polimi.ingsw.gc28.network.messages.client;

import it.polimi.ingsw.gc28.controller.GameController;
import it.polimi.ingsw.gc28.model.errors.PlayerActionError;
import it.polimi.ingsw.gc28.model.errors.types.FailedActionManaged;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Optional;

/**
 * Message sent from Client To Server
 */
public abstract class MessageC2S implements Serializable {
    private final MessageTypeC2S type;

    private String gameId;

    protected MessageC2S(MessageTypeC2S messageType, String gameId) {
        this.type = messageType;
        this.gameId = gameId;
    }

    protected  MessageC2S(MessageTypeC2S messageType){
        this.type = messageType;
    }

    public Optional<String> getGameId() {
        return Optional.ofNullable(gameId);
    }

    public abstract void execute(GameController controller) throws RemoteException, FailedActionManaged;

    public MessageTypeC2S getType() {
        return type;
    }

    public void attachVirtualView(VirtualView client){}
}
