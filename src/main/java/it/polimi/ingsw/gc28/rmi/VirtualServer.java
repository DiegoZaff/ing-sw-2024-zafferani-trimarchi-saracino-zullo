package it.polimi.ingsw.gc28.rmi;

import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.cards.CardObjective;
import it.polimi.ingsw.gc28.model.cards.CardResource;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualServer extends Remote {
     void connect(VirtualView client) throws RemoteException;
     void addPlayerToGame(String clientId, String name) throws  RemoteException;


     void playGameCard (Player playingPlayer, CardGame playedCard, boolean isFront, Coordinate coordinates ) throws RemoteException;

     void drawGameCard(Player playingPlayer, boolean fromGoldDeck) throws  RemoteException;

     void drawGameCard(Player playingPlayer, CardResource CardToDraw, boolean fromGoldVisible) throws  RemoteException;

     void chooseObjective(String clientId, int n) throws  RemoteException;

}
