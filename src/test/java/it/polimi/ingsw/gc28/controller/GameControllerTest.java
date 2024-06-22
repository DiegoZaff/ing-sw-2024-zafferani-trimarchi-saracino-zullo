package it.polimi.ingsw.gc28.controller;

import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.cards.CardInitial;
import it.polimi.ingsw.gc28.model.cards.CardObjective;
import it.polimi.ingsw.gc28.model.chat.ChatMessage;
import it.polimi.ingsw.gc28.network.messages.client.MsgJoinGame;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;
import it.polimi.ingsw.gc28.model.errors.PlayerActionError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * this class tests the methods from GameController
 */
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

//        System.out.println("gioco inizia");

        ArrayList<Player> playersCopy = game.getPlayers();

        Player p = (gameController.gameModel.playerToPlay().get());


        ArrayList<Player> players2 = new ArrayList<>(gameController.gameModel.getPlayers());

        // Rimuovi il giocatore che gioca per primo dalla lista
        players2.remove(p);

        // Assegna l'altro giocatore a p2
        Player p2 = players2.getFirst();

        players2.add(p);

//        System.out.println("p è " + p.getName());
//        System.out.println("p2 è " + p2.getName());
//
//        System.out.println("action_" + gameController.gameModel.actionExpected().toString());
//        System.out.println("First_" +gameController.gameModel.playerToPlay().get().getName());



        gameController.chooseColor(p.getName(), "hhh"); //invalid color
        gameController.chooseColor(p.getName(), "RED");
        gameController.chooseColor(p.getName(), "BLUE"); //invalid move

//        System.out.println("dopo p1 colore");

//        System.out.println("action_" + gameController.gameModel.actionExpected().toString());
//        System.out.println("First_" + gameController.gameModel.playerToPlay().get().getName());

        gameController.chooseColor(p2.getName(), "RED"); // already used color
        gameController.chooseColor(p2.getName(), "BLUE");
////        System.out.println("dopo che tutti hanno scelto il colore");
//
//        System.out.println("action_" + gameController.gameModel.actionExpected().toString());
//        System.out.println("First_" + gameController.gameModel.playerToPlay().get().getName());

        CardInitial c1 = p.getCardInitial().get();
        CardInitial c2 = p2.getCardInitial().get();

        gameController.playCard(p.getName(), c1.getId(), false, new Coordinate(0,0) );

//        System.out.println("dopo che p1 ha giocato carta iniziale");
//
//        System.out.println("action_" + gameController.gameModel.actionExpected().toString());
//        System.out.println("First_" + gameController.gameModel.playerToPlay().get().getName());


        gameController.playCard(p2.getName(), "INI_90", false, new Coordinate(0,0) ); //invalid id
        gameController.playCard(p2.getName(), "INI_2", false, new Coordinate(0,0) ); // card not playable

        gameController.playCard(p2.getName(), c2.getId(), false, new Coordinate(0,0) );

//        System.out.println("dopo che tutti hanno giocato carta iniziale");
//
//        System.out.println("action_" + gameController.gameModel.actionExpected().toString());
//        System.out.println("First_" + gameController.gameModel.playerToPlay().get().getName());



        CardObjective o1 = p.getObjectivesToChoose().get().getFirst();
        CardObjective o2 = p2.getObjectivesToChoose().get().getFirst();


        gameController.chooseObjectivePersonal(p.getName(), "OBJ_1"); // obj not owned
        gameController.chooseObjectivePersonal(p.getName(), o1.getId());
        gameController.chooseObjectivePersonal(p.getName(), o1.getId()); //not you turn
//        System.out.println("dopo che p1 ha scelto obiettivo");
//
//        System.out.println("action_" + gameController.gameModel.actionExpected().toString());
//        System.out.println("First_" + gameController.gameModel.playerToPlay().get().getName());


        gameController.chooseObjectivePersonal(p2.getName(), o2.getId());

//        System.out.println("dopo che p2 ha scelto obiettivo");
//
//        System.out.println("action_" + gameController.gameModel.actionExpected().toString());
//        System.out.println("First_" + gameController.gameModel.playerToPlay().get().getName());

        CardGame cardGame1 = p.gethand().getFirst();
        CardGame cardGame2 = p2.gethand().getFirst();

        gameController.playCard(p.getName(), cardGame1.getId(), false, new Coordinate(9,9)); // invalid coords
        gameController.playCard(p.getName(), "RES_20", false, new Coordinate(1,1)); // card not owned
        gameController.playCard(p.getName(), cardGame1.getId(), false, new Coordinate(1,1));
        gameController.playCard(p.getName(), cardGame1.getId(), false, new Coordinate(1,1)); // not your turn

        gameController.drawCard(p.getName(), true);
        gameController.playCard(p.getName(), cardGame1.getId(), false, new Coordinate(1,1)); // invalid action

//        System.out.println("dopo che il primo ha giocato e pescato carta");
//
//
//        System.out.println("action_" + gameController.gameModel.actionExpected().toString());
//        System.out.println("First_" + gameController.gameModel.playerToPlay().get().getName());



        gameController.playCard(p2.getName(), cardGame2.getId(), false, new Coordinate(1,1));

//        System.out.println("dopo aver giocato carta");
//
//        System.out.println("action_" + gameController.gameModel.actionExpected().toString());
//        System.out.println("First_" + gameController.gameModel.playerToPlay().get().getName());


        gameController.drawCard(p2.getName(), false);

//        System.out.println("dopo che p2 ha finito");
//
//        System.out.println("action_" + gameController.gameModel.actionExpected().toString());
//        System.out.println("First_" + gameController.gameModel.playerToPlay().get().getName());

        CardGame cardGame3 = p.gethand().get(1);
        CardGame cardGame4 = p2.gethand().get(1);

        gameController.playCard(p.getName(), cardGame3.getId(), false, new Coordinate(-1,-1));

//        System.out.println("AAAdopo che 1 ha giocato seconda carta");
//
//        System.out.println("action_" + gameController.gameModel.actionExpected().toString());
//        System.out.println("First_" + gameController.gameModel.playerToPlay().get().getName());

        CardGame cardToDraw1 = game.getFaceUpGoldCards().getFirst();
        gameController.drawCard(p.getName(), cardToDraw1.getId());

        gameController.playCard(p2.getName(), cardGame4.getId(), false, new Coordinate(-1,-1));
        CardGame cardToDraw2 = game.getFaceUpResourceCards().getFirst();

        gameController.drawCard(p2.getName(), "GOLD_100"); // invalid id
        gameController.drawCard(p2.getName(), "GOLD_1"); // card not drawable

        gameController.drawCard(p2.getName(), cardToDraw2.getId());
        gameController.drawCard(p2.getName(), cardToDraw2.getId()); // not your turn

//        System.out.println("dopo che tutti giocato e pescato la seconda carta");
//        System.out.println("action_" + gameController.gameModel.actionExpected().toString());
//        System.out.println("First_" + gameController.gameModel.playerToPlay().get().getName());

        ChatMessage chatMessage1 = new ChatMessage("siummico", p.getName(), p2.getName(), false);
        ChatMessage chatMessage2 = new ChatMessage("siummico privato", p.getName(), p2.getName(), true);
        ChatMessage chatMessage3 = new ChatMessage("siummico all", p.getName(), "all", false);
        ChatMessage chatMessage5 = new ChatMessage("siummico all", p.getName(), "all", true);  //error

        ChatMessage chatMessage4 = new ChatMessage("errore", p.getName(), "Michele Trombetta", true); //no reciever
        ChatMessage chatMessage6 = new ChatMessage("errore", p.getName(), "Michele Trombetta", true); // no rieciever



        gameController.sendMessage(chatMessage1);
        gameController.sendMessage(chatMessage2);
        gameController.sendMessage(chatMessage3);
        gameController.sendMessage(chatMessage4);
        gameController.sendMessage(chatMessage5);
        gameController.sendMessage(chatMessage6);

        System.out.println(game.getChat());
        System.out.println(game.getChat().toString(p.getName(), p2.getName()));

        gameController.waitForReconnections();

        gameController.reconnect("player1", client);
        gameController.reconnect("player2", client2);

        gameController.hasGameRestarted();



    }


}

