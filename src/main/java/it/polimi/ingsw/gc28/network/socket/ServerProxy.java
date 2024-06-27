package it.polimi.ingsw.gc28.network.socket;

import it.polimi.ingsw.gc28.network.messages.client.*;
import it.polimi.ingsw.gc28.network.rmi.VirtualServer;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class ServerProxy implements VirtualServer {
    final ObjectOutputStream output;

    public ServerProxy(ObjectOutputStream output) {
        this.output = output;
    }

    public void sendMessage(MessageC2S message) throws IOException {

            output.writeObject(message);
            finishSending();

    }

//    @Override
//    public void createGame(VirtualView client, String userName, int numberOfPlayers) throws RemoteException {
//        sendMessage(new MsgCreateGame(null, client, userName, numberOfPlayers));
//    }
//
//    @Override
//    public void joinGame(VirtualView client, String gameId, String userName) throws RemoteException {
//        sendMessage(new MsgJoinGame(client, gameId, userName));
//    }
//
//    @Override
//    public void playGameCard(String playerName, String cardId, String gameId, boolean isFront, Coordinate coordinate) throws RemoteException {
//        sendMessage(new MsgPlayGameCard(playerName, cardId, gameId, isFront, coordinate));
//    }
//
//    @Override
//    public void drawGameCard(String playerName, String gameId, boolean fromGoldDeck) throws RemoteException {
//        sendMessage(new MsgDrawGameCard(playerName, gameId, fromGoldDeck));
//    }
//
//    @Override
//    public void drawGameCard(String playerName, String gameId, String cardId) throws RemoteException {
//        sendMessage(new MsgDrawnVisibleGameCard(playerName, gameId, cardId));
//    }
//
//    @Override
//    public void chooseObjective(String playerName, String gameId, String cardId) throws RemoteException {
//        sendMessage(new MsgChooseObjective(playerName, gameId, cardId));
//    }

    /**
     * This method ensures that everything written to the output steam is actually sent and the steam is ready for
     * further operations.
     */
    public void finishSending() throws IOException {
        output.flush();
        output.reset();
    }
}
