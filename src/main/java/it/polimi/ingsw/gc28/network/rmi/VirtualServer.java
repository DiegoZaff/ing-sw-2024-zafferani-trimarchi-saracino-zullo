package it.polimi.ingsw.gc28.network.rmi;

import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.cards.CardResource;
import it.polimi.ingsw.gc28.network.messages.client.MessageC2S;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * These are called from the client and will act on the server.
 */

// TODO : uniform virtual servers
public interface VirtualServer extends Remote {
     /**
      * This method is for creating a new game. The user who creates the game, also joins it by providing a username.
      * @param client the virtualView associated with the client
      * @param userName the nickname associated to the player who creates the game.
      */
     void createGame(VirtualView client, String userName, int numberOfPlayers) throws RemoteException;

     void sendMessage(MessageC2S message) throws RemoteException;

     /**
      * This method is for joining an already created game. User must provide nickname.
      */
     void joinGame(VirtualView client, String gameId, String userName) throws  RemoteException;

     /**
      * This method if for playing a card.
      */
     void playGameCard (String playerName, String cardId, String gameId, boolean isFront, Coordinate coordinate ) throws RemoteException;

     /**
      * This method is for drawing a card from goldDeck or resourceDeck
      */
     void drawGameCard(String playerName, String gameId, boolean fromGoldDeck) throws  RemoteException;

     /**
      * This method is for drawing a card from the visible cards.
      */
     void drawGameCard(String playerName, String gameId, String cardId) throws  RemoteException;

     /**
      * This method is for choosing the objective.
      */
     void chooseObjective(String playerName, String gameId, String cardId) throws  RemoteException;

}
