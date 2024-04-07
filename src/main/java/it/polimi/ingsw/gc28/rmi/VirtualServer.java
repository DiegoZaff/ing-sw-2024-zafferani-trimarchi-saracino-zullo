package it.polimi.ingsw.gc28.rmi;

import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.cards.CardObjective;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualServer extends Remote {
    public void connect(VirtualView client) throws RemoteException;

    public void addPlayerToGame(String name) throws  RemoteException;

    public void playGameCard (Player playingPlayer, CardGame playedCard, boolean isFront, Coordinate coordinates ) throws RemoteException;

    public void drawGameCard(Player playingPlayer, boolean fromGoldDeck) throws  RemoteException;

    public void drawGameCard(Player playingPlayer, CardGame CardToDraw) throws  RemoteException;

    public void chooseObjective(Player player, CardObjective card) throws  RemoteException;

}
