package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.view.GameManagerClient;


import java.io.Serializable;

/**
 * Abstract class that represent a generic message from Server to Client.
 */
public abstract class MessageS2C implements Serializable {
    private final MessageTypeS2C type;

    protected MessageS2C(MessageTypeS2C type) {
        this.type = type;
    }


    public abstract void update(GameManagerClient gameManagerClient, boolean isCli);

    public MessageTypeS2C getType() {
        return type;
    }
}
