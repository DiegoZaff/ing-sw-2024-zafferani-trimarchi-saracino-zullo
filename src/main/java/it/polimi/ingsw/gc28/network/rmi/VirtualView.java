package it.polimi.ingsw.gc28.network.rmi;

import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.Table;
import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.network.messages.client.MessageC2S;
import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


/**
 * This is called from the server, and will act on the client.
 */
public interface VirtualView extends Remote {
     public abstract void sendMessage(MessageS2C message);

     void onGameCreated(String gameId, String playerName, int playersLeftToJoin) throws RemoteException;

     void onGameJoined(String gameId, String playerName, int playersLeftToJoin) throws RemoteException;

     void onGameStarted(ArrayList<Player> players) throws RemoteException;

     void onPlayerPlayedCard(String playerName, Table newTable, int newPlayerPoints) throws RemoteException;

     void onPlayerDrawnCard(String playerName, String cardId, boolean fromGoldDeck) throws RemoteException;

     void onPlayerDrawnCard(String playerName, String cardId) throws RemoteException;

     void onPlayerChoseObjective(String playerName, String cardId) throws RemoteException;

     void reportError(String details) throws RemoteException;

     void reportMessage(String details) throws RemoteException;

     void onNextExpectedPlayerAction(ActionType actionType, String playerOfTurn) throws RemoteException;
}
