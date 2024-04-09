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
public interface VirtualServer extends Remote {
     void handleMessage(MessageC2S message) throws RemoteException;
//     void connect(VirtualView client) throws RemoteException;
//     void addPlayerToGame(String clientId, String name) throws  RemoteException;
//
//     void playGameCard (Player playingPlayer, CardGame playedCard, boolean isFront, Coordinate coordinates ) throws RemoteException;
//
//     void drawGameCard(Player playingPlayer, boolean fromGoldDeck) throws  RemoteException;
//
//     void drawGameCard(Player playingPlayer, CardResource CardToDraw, boolean fromGoldVisible) throws  RemoteException;
//
//     void chooseObjective(String clientId, int n) throws  RemoteException;

}
