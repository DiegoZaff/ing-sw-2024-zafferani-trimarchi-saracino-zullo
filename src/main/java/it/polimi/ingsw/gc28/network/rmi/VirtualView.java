package it.polimi.ingsw.gc28.network.rmi;

import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.Table;
import it.polimi.ingsw.gc28.network.messages.client.MessageC2S;
import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


/**
 * These are called from the server, and will act on the client.
 */
public interface VirtualView extends Remote {

     /**
      * This method notifies to the client that the game was created successfully, providing gameId and number of
      * players needed to start the game and confirming playerName.
      */
     void onGameCreated(String gameId, String playerName, int playersLeftToJoin) throws RemoteException;

     /**
      * This method notifies to the client that the game was joined successfully, providing gameId and number of
      * players needed to start the game and confirming playerName.
      */
     void onGameJoined(String gameId, String playerName, int playersLeftToJoin) throws RemoteException;

     /**
      * This method notifies the client about the starting hands of the players, their names and so on...
      */
     void onGameStarted(ArrayList<Player> players) throws RemoteException;

     void onPlayerPlayedCard(String playerName, Table newTable, int newPlayerPoints, ArrayList<Coordinate> newPlayableCoords) throws RemoteException;

     /**
      * when a player draws from a deck.
      */
     void onPlayerDrawnCard(String playerName, String cardId, boolean fromGoldDeck) throws RemoteException;

     /**
      * when a player draws from the visible cards
      */
     void onPlayerDrawnCard(String playerName, String cardId) throws RemoteException;

     void onPlayerChoseObjective(String playerName, String cardId) throws RemoteException;

     /**
      * Report an error when an action can't be done.
      */
     void reportError(String details) throws RemoteException;

     void reportMessage(String details) throws RemoteException;

}
