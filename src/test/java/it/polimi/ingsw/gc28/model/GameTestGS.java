package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.chat.ChatMessage;
import it.polimi.ingsw.gc28.model.errors.PlayerActionError;
import it.polimi.ingsw.gc28.view.GameRepresentation;
import org.junit.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * this class tests more methods from Game
 */
public class GameTestGS {
@Test
    public void getset() throws IOException, PlayerActionError {
        Game game = new Game( 2, "0");
        String id = game.generateRandomGameId();
        Game game2 = new Game( 2, id);

        assertEquals(game2.getGameId(), id, "id");
        Player p1 = new Player("aaaaa");
        Player p2 = new Player("bbbbb");
        Player p3 = new Player("ccccc");
        game2.addPlayerToGame(p1.getName());
        assertEquals(game2.getPlayersToJoin(), 1, "nplayer");
        game2.addPlayerToGame(p2.getName());
        assertEquals(game2.getPlayersToJoin(), 0, "nplayer");

        GameRepresentation gameRepresentation = new GameRepresentation();

        game2.getGameRepresentation();


        game2.setWaitForReconnections();
        game2.reconnectPlayer(p1.getName());
        assertEquals(game2.getNPlayersToReconnect(), 1, "id");
        game2.reconnectPlayer(p2.getName());
        assertEquals(game2.isEveryoneReconnected(), true, "id");

        assertEquals(p2.isConnected(), true, "id");
        assertEquals(p1.isConnected(), true, "id");

        game2.sendMessage(new ChatMessage( "msg pubblico", p1.getName(), p2.getName(), false));
        game2.sendMessage(new ChatMessage( "msg privato", p1.getName(), p2.getName(), true));

        System.out.println("messaggi inviati in chat globale");
        System.out.println(game2.getChat().toString());
        System.out.println("messaggi inviati in chat priv");
        System.out.println(game2.getChat().toString(p2.getName(), p1.getName()));
        System.out.println("messaggi inviati in chat priv no mess");
        System.out.println(game2.getChat().toString(p3.getName(), p1.getName()));

    }
}
