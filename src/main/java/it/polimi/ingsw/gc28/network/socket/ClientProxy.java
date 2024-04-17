package it.polimi.ingsw.gc28.network.socket;

import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.Table;
import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;
import it.polimi.ingsw.gc28.network.messages.server.MsgOnGameCreated;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ClientProxy implements VirtualView {
    final ObjectOutputStream output;

    public ClientProxy(ObjectOutputStream output) {
        this.output = output;
    }

    public void sendMessage(MessageS2C message)  {
        try{
            output.writeObject(message);
        }catch (IOException e){
            System.err.println("Error writing object to output stream: " + e.getMessage());
        }
    }

    @Override
    public void onGameCreated(String gameId, String playerName, int playersLeftToJoin) throws RemoteException {
    }

    @Override
    public void onGameJoined(String gameId, String playerName, int playersLeftToJoin) throws RemoteException {
        //synchronized
        sendMessage(new MsgOnGameCreated(gameId, playerName, playersLeftToJoin));
    }

    @Override
    public void onGameStarted(ArrayList<Player> players) throws RemoteException {

    }

    @Override
    public void onPlayerPlayedCard(String playerName, Table newTable, int newPlayerPoints, ArrayList<Coordinate> newPlayableCoords) throws RemoteException {

    }

    @Override
    public void onPlayerDrawnCard(String playerName, String cardId, boolean fromGoldDeck) throws RemoteException {

    }

    @Override
    public void onPlayerDrawnCard(String playerName, String cardId) throws RemoteException {

    }

    @Override
    public void onPlayerChoseObjective(String playerName, String cardId) throws RemoteException {

    }

    @Override
    public void reportError(String details) throws RemoteException {

    }

    @Override
    public void reportMessage(String details) throws RemoteException {

    }
}
