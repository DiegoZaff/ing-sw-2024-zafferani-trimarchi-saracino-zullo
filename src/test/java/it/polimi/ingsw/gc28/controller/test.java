package it.polimi.ingsw.gc28.controller;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import it.polimi.ingsw.gc28.controller.GameController;
import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.easymock.EasyMock.*;

import java.io.IOException;
import java.rmi.RemoteException;
public class test {
    private GameController gameController;
    private Game mockGame;
    private VirtualView mockView;

    @BeforeEach
    public void setUp() throws IOException {
        mockGame = createMock(Game.class);
        mockView = createMock(VirtualView.class);
        gameController = new GameController(mockGame);
    }


    @Test
    public void testAddPlayerToGame() throws RemoteException {
        String playerName = "player1";

        gameController.addPlayerToGame(playerName, mockView);

        assertEquals(1, gameController.getPlayersToJoin());
        //assertTrue(gameController.getClients().containsKey(playerName));
    }
}

//    @Test
//    public void testChooseColor() throws RemoteException {
//        String playerName = "player1";
//        gameController.addPlayerToGame(playerName, mockView);
//
//        gameController.chooseColor(playerName, "red");
//
//        verify(mockGame, times(1)).setPlayerColor(playerName, "red");
//    }
//
//    @Test
//    public void testPlayCard() throws RemoteException {
//        String playerName = "player1";
//        gameController.addPlayerToGame(playerName, mockView);
//        Coordinate mockCoordinate = mock(Coordinate.class);
//
//        gameController.playCard(playerName, "card1", true, mockCoordinate);
//
//        verify(mockGame, times(1)).playCard(playerName, "card1", true, mockCoordinate);
//    }
//}

