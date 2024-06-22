package it.polimi.ingsw.gc28.controller;

import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.network.messages.client.MsgCreateGame;
import it.polimi.ingsw.gc28.network.messages.client.MsgJoinGame;
import it.polimi.ingsw.gc28.network.messages.client.MsgJoinableGames;
import it.polimi.ingsw.gc28.network.messages.client.MsgReconnect;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.*;

/**
 * this class tests the methods from both GameManager and GameController
 */
public class MixedControllerTest {


    private GamesManager gamesManager;

    private VirtualView mockView1;
    private VirtualView mockView2;
    private VirtualView mockView3;
    private GameController gameController;


    @BeforeEach
    public void setUp() {
        gamesManager = GamesManager.getInstance();

        mockView1 = createMock(VirtualView.class);
        mockView2 = createMock(VirtualView.class);
        mockView3 = createMock(VirtualView.class);
        //mockController = createMock(GameController.class);

   }

    @Test
    public void testCreateGame() throws RemoteException {
        MsgCreateGame msg = new MsgCreateGame("0", "aaaa", 2);
        msg.setClient(mockView1);
        gamesManager.createGame(msg);

        Map<String, GameController> partite;
        partite = gamesManager.getMapGames();
        List<Map.Entry<String, GameController>>lista = partite.entrySet().stream().toList();
        String id = lista.get(0).getKey();



        MsgJoinGame msgJoinGame = new MsgJoinGame(id, "bbbbb");
        msgJoinGame.setClient(mockView2);
        gamesManager.addMessageToQueue(msgJoinGame);



        gamesManager.restoreGame(gamesManager.getGameController(id).get().gameModel);

        gamesManager.getGameController(id).get().gameModel.getPlayers().get(1);
        MsgReconnect msgReconnect = new MsgReconnect(id, "bbbbb");
        msgReconnect.setClient(mockView2);
        gamesManager.addMessageToQueue(msgReconnect);
        gamesManager.addMessageToQueue(msgReconnect);


        MsgJoinableGames msgJoinableGames = new MsgJoinableGames();
        msgJoinableGames.setClient(mockView3);
        gamesManager.addMessageToQueue(msgJoinableGames);


        gamesManager.getGameController(id).get().addMessageToQueue(msgJoinableGames);

    }

    @Test
    public void testCreateGame2() throws IOException {

        Game gameModel = new Game( 2, "0");
        GameController gameController1 = new GameController(gameModel);




        gameController1.addPlayerToGame("aaaaa", mockView1);
        gameController1.addPlayerToGame("bbbbb", mockView2);

        gameController1.hasGameStarted();


    }

}

