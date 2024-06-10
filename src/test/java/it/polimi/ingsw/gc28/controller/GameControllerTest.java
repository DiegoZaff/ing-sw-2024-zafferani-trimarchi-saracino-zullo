package it.polimi.ingsw.gc28.controller;

import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.cards.CardInitial;
import it.polimi.ingsw.gc28.model.cards.CardObjective;
import it.polimi.ingsw.gc28.model.chat.ChatMessage;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;
import it.polimi.ingsw.gc28.model.errors.PlayerActionError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class GameControllerTest {
    private GameController gameController;
    private Game gameMock;
    private Game game;
    private VirtualView clientMock;
    private VirtualView client;
    private VirtualView client2;

    @BeforeEach
    public void setUp() throws IOException {
        //gameMock = mock(Game.class);
        //clientMock = mock(VirtualView.class);


        game = new Game(2, "0");
        gameController = new GameController(game);

        client = createMock(VirtualView.class);
        client2 = createMock(VirtualView.class);



    }

    @Test
    public void testAddPlayerToGame_Success() throws RemoteException, PlayerActionError {
        //when(game.addPlayerToGame(anyString())).thenReturn(true);




        gameController.addPlayerToGame("player1", client);
        gameController.addPlayerToGame("player2", client2);
        gameController.hasGameStarted();

        System.out.println("gioco inizia");

        ArrayList<Player> playersCopy = game.getPlayers();
//        System.out.println(players.get(0).getName());
//        System.out.println(players.get(1).getName());
        Player p = (gameController.gameModel.playerToPlay().get());


        ArrayList<Player> players2 = new ArrayList<>(gameController.gameModel.getPlayers());

        // Rimuovi il giocatore che gioca per primo dalla lista
        players2.remove(p);

        // Assegna l'altro giocatore a p2
        Player p2 = players2.getFirst();

        players2.add(p);

        System.out.println("p è " + p.getName());
        System.out.println("p2 è " + p2.getName());

        System.out.println("action_" + gameController.gameModel.actionExpected().toString());
        System.out.println("First_" +gameController.gameModel.playerToPlay().get().getName());

        //gameController.chooseColor(p.getName(), "RED");
//        gameController.chooseColor("player1", "YELLOW");

        gameController.chooseColor(p.getName().toString(), "RED");

        System.out.println("dopo p1 colore");

        System.out.println("action_" + gameController.gameModel.actionExpected().toString());
        System.out.println("First_" + gameController.gameModel.playerToPlay().get().getName());


        gameController.chooseColor(p2.getName().toString(), "BLUE");
        System.out.println("dopo che tutti hanno scelto il colore");

        System.out.println("action_" + gameController.gameModel.actionExpected().toString());
        System.out.println("First_" + gameController.gameModel.playerToPlay().get().getName());

        CardInitial c1 = p.getCardInitial().get();
        CardInitial c2 = p2.getCardInitial().get();

        gameController.playCard(p.getName(), c1.getId().toString(), false, new Coordinate(0,0) );

        System.out.println("dopo che p1 ha giocato carta iniziale");

        System.out.println("action_" + gameController.gameModel.actionExpected().toString());
        System.out.println("First_" + gameController.gameModel.playerToPlay().get().getName());



        gameController.playCard(p2.getName(), c2.getId().toString(), false, new Coordinate(0,0) );

        System.out.println("dopo che tutti hanno giocato carta iniziale");

        System.out.println("action_" + gameController.gameModel.actionExpected().toString());
        System.out.println("First_" + gameController.gameModel.playerToPlay().get().getName());



        CardObjective o1 = p.getObjectivesToChoose().get().get(0);
        CardObjective o2 = p2.getObjectivesToChoose().get().get(0);



        gameController.chooseObjectivePersonal(p.getName().toString(), o1.getId().toString());
        System.out.println("dopo che p1 ha scelto obiettivo");

        System.out.println("action_" + gameController.gameModel.actionExpected().toString());
        System.out.println("First_" + gameController.gameModel.playerToPlay().get().getName());


        gameController.chooseObjectivePersonal(p2.getName().toString(), o2.getId().toString());

        System.out.println("dopo che p2 ha scelto obiettivo");

        System.out.println("action_" + gameController.gameModel.actionExpected().toString());
        System.out.println("First_" + gameController.gameModel.playerToPlay().get().getName());

        CardGame cardGame1 = p.gethand().get(0);
        CardGame cardGame2 = p2.gethand().get(0);

        gameController.playCard(p.getName(), cardGame1.getId().toString(), false, new Coordinate(1,1));
        gameController.drawCard(p.getName().toString(), true);

        System.out.println("dopo che il primo ha giocato e pescato carta");


        System.out.println("action_" + gameController.gameModel.actionExpected().toString());
        System.out.println("First_" + gameController.gameModel.playerToPlay().get().getName());



        gameController.playCard(p2.getName(), cardGame2.getId().toString(), false, new Coordinate(1,1));

        System.out.println("dopo aver giocato carta");

        System.out.println("action_" + gameController.gameModel.actionExpected().toString());
        System.out.println("First_" + gameController.gameModel.playerToPlay().get().getName());


        gameController.drawCard(p2.getName().toString(), false);

        System.out.println("dopo che p2 ha finito");

        System.out.println("action_" + gameController.gameModel.actionExpected().toString());
        System.out.println("First_" + gameController.gameModel.playerToPlay().get().getName());

        CardGame cardGame3 = p.gethand().get(1);
        CardGame cardGame4 = p2.gethand().get(1);

        gameController.playCard(p.getName(), cardGame3.getId().toString(), false, new Coordinate(-1,-1));

        System.out.println("AAAdopo che 1 ha giocato seconda carta");

        System.out.println("action_" + gameController.gameModel.actionExpected().toString());
        System.out.println("First_" + gameController.gameModel.playerToPlay().get().getName());

        CardGame cardToDraw1 = game.getFaceUpGoldCards().get(0);
        gameController.drawCard(p.getName().toString(), cardToDraw1.getId());


        gameController.playCard(p2.getName(), cardGame4.getId().toString(), false, new Coordinate(-1,-1));
        CardGame cardToDraw2 = game.getFaceUpResourceCards().get(0);
        gameController.drawCard(p2.getName().toString(),cardToDraw2.getId().toString() );

        System.out.println("dopo che tutti giocato e pescato la seconda carta");
        System.out.println("action_" + gameController.gameModel.actionExpected().toString());
        System.out.println("First_" + gameController.gameModel.playerToPlay().get().getName());

        ChatMessage chatMessage1 = new ChatMessage("siummico", p.getName(), p2.getName(), false);
        ChatMessage chatMessage2 = new ChatMessage("siummico privato", p.getName(), p2.getName(), true);


        gameController.sendMessage(chatMessage1);
        gameController.sendMessage(chatMessage2);

        System.out.println(game.getChat());
        System.out.println(game.getChat().toString(p.getName(), p2.getName()));
        //game.
        //gameController.chooseObjectivePersonal("player1", "OBJ_1");
        //verify(game, times(1)).addPlayerToGame("player1");
    }

//    @Test
//    public void testAddPlayerToGame_Failure() throws RemoteException, PlayerActionError {
//        String playerName = "testPlayer";
//        PlayerActionError error = new PlayerActionError("Error");
//        doThrow(error).when(gameMock).addPlayerToGame(playerName);
//
//        boolean result = gameController.addPlayerToGame(playerName, clientMock);
//
//        assertFalse(result);
//        verify(gameMock, times(1)).addPlayerToGame(playerName);
//        verify(clientMock, times(1)).sendMessage(any());
//    }
//
//    @Test
//    public void testDrawCard_Success() throws RemoteException, PlayerActionError {
//        String playerName = "testPlayer";
//        CardResource cardMock = mock(CardResource.class);
//        when(gameMock.drawGameCard(playerName, true)).thenReturn(cardMock);
//
//        gameController.drawCard(playerName, true);
//
//        verify(gameMock, times(1)).drawGameCard(playerName, true);
//        verify(clientMock, never()).sendMessage(any());
//    }
//
//    @Test
//    public void testDrawCard_Failure() throws RemoteException, PlayerActionError {
//        String playerName = "testPlayer";
//        PlayerActionError error = new PlayerActionError("Error");
//        doThrow(error).when(gameMock).drawGameCard(playerName, true);
//
//        gameController.drawCard(playerName, true);
//
//        verify(gameMock, times(1)).drawGameCard(playerName, true);
//        verify(clientMock, times(1)).sendMessage(any());
//    }
}

