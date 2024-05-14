package it.polimi.ingsw.gc28.network.rmi;

import it.polimi.ingsw.gc28.controller.GameController;
import it.polimi.ingsw.gc28.network.messages.client.MessageC2S;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GameStub extends UnicastRemoteObject implements VirtualStub {

    private final GameController controller;

    final private String playerName;

    final private String gameId;

    public GameStub(GameController controller, String playerName, String gameId)  throws RemoteException {
        this.controller = controller;
        this.playerName = playerName;
        this.gameId = gameId;
    }


    @Override
    public void sendMessage(MessageC2S message) throws RemoteException {
        controller.addMessageToQueue(message);
    }


    public GameController getController() {
        return controller;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getGameId() {
        return gameId;
    }
}
