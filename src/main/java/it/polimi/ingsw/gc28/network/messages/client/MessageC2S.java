package it.polimi.ingsw.gc28.network.messages.client;

import java.util.Optional;

/**
 * Message sent from Client To Server
 */
public abstract class MessageC2S {

    private String gameId;

    protected MessageC2S(String gameId) {
        this.gameId = gameId;
    }

    public Optional<String> getGameId() {
        return Optional.ofNullable(gameId);
    }
}
