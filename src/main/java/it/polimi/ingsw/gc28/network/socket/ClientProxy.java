package it.polimi.ingsw.gc28.network.socket;

import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.Table;
import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.network.messages.server.*;
import it.polimi.ingsw.gc28.network.rmi.GameStub;
import it.polimi.ingsw.gc28.network.rmi.VirtualServer;
import it.polimi.ingsw.gc28.network.rmi.VirtualStub;
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
    @Override
    public void sendMessage(MessageS2C message)  {
        try{
            output.writeObject(message);
            finishSending();
        }catch (IOException e){
            System.err.println("Error writing object to output stream: " + e.getMessage());
        }
    }

    @Override
    public void attachGameStub(VirtualStub gameStub) throws RemoteException {

    }

   /* @Override
    public void onGameCreated(String gameId, String playerName, int playersLeftToJoin) throws RemoteException {
        sendMessage(new MsgOnGameCreated(gameId, playerName, playersLeftToJoin));
    }

    @Override
    public void onGameJoined(String gameId, String playerName, int playersLeftToJoin) throws RemoteException {
        sendMessage(new MsgOnGameJoined(gameId, playerName, playersLeftToJoin));
    }

    @Override
    public void onGameStarted(ArrayList<Player> players) throws RemoteException {
        sendMessage(new MsgOnGameStarted(players));
    }

    @Override
    public void onPlayerPlayedCard(String playerName, Table newTable, int newPlayerPoints) throws RemoteException {
        sendMessage(new MsgOnPlayerPlayedCard(playerName, newTable, newPlayerPoints));

    }

    @Override
    public void onPlayerDrawnCard(String playerName, String cardId, boolean fromGoldDeck) throws RemoteException {
        sendMessage(new MsgOnPlayerDrawnVisibleCard(playerName, cardId, fromGoldDeck));
    }

    @Override
    public void onPlayerDrawnCard(String playerName, String cardId) throws RemoteException {
        sendMessage(new MsgOnPlayerDrawnCard(playerName, cardId));
    }

    @Override
    public void onPlayerChoseObjective(String playerName, String cardId) throws RemoteException {
        sendMessage(new MsgOnPlayerChooseObjective(playerName, cardId));
    }

    @Override
    public void reportError(String details) throws RemoteException {
        sendMessage(new MsgReportError(details));
    }

    @Override
    public void reportMessage(String details) throws RemoteException {
        sendMessage(new MsgReportMessage(details));
    }

    @Override
    public void onNextExpectedPlayerAction(ActionType actionType, String playerOfTurn) throws RemoteException {
        sendMessage(new MsgOnNextExpectedPlayerAction(actionType, playerOfTurn));
    }*/

    /**
     * This method ensures that everything written to the output steam is actually sent and the steam is ready for
     * further operations.
     */
    public void finishSending() throws IOException {
        output.flush();
        output.reset();
    }
}
