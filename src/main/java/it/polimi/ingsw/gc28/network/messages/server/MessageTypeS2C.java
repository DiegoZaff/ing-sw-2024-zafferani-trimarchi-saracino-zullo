package it.polimi.ingsw.gc28.network.messages.server;

public enum MessageTypeS2C {
    CHAT,
    GAME_CREATED,
    GAME_JOINED,
    GAME_STARTED,
    CHOOSE_COLOR,
    PLAYER_ACTION,
    CHOOSE_OBJ,
    DRAW_CARD,
    PLAY_CARD,
    ERROR,
    REPORT,
    RECONNECTED,
    GAME_RESTARTED,
    SOMEONE_ELSE_JOINED,
    SOMEONE_ELSE_RECONNECTED,
    JOINABLE_GAMES,
    PING,
    TERMINATION,
    IS_WINNERS
}
