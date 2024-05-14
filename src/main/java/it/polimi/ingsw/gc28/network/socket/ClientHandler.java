package it.polimi.ingsw.gc28.network.socket;


import it.polimi.ingsw.gc28.controller.GameController;
import it.polimi.ingsw.gc28.controller.GamesManager;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.Table;
import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.network.messages.client.MessageC2S;
import it.polimi.ingsw.gc28.network.messages.client.MessageTypeC2S;
import it.polimi.ingsw.gc28.network.messages.client.MsgCreateGame;
import it.polimi.ingsw.gc28.network.messages.client.MsgJoinGame;
import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;
import it.polimi.ingsw.gc28.network.messages.server.MsgOnGameCreated;
import it.polimi.ingsw.gc28.network.rmi.GameStub;
import it.polimi.ingsw.gc28.network.rmi.VirtualServer;
import it.polimi.ingsw.gc28.network.rmi.VirtualStub;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ClientHandler implements VirtualView {
    final ServerTCP server;
    final ObjectInputStream input;
    final ClientProxy clientProxy;
    final Socket clientSocket;
    private GameController controller;

    public ClientHandler(ServerTCP server, ObjectInputStream input, ObjectOutputStream output, Socket clientSocket) {
        this.server = server;
        this.input = input;
        this.clientProxy = new ClientProxy(output);
        this.clientSocket = clientSocket;
    }

    public void runVirtualView() throws IOException {

        try {
            MessageC2S receivedMessage = null;
            while ((receivedMessage = (MessageC2S) input.readObject()) != null) {
                System.out.println("Received message from client: " + receivedMessage);

                if(receivedMessage.getType().equals(MessageTypeC2S.CREATE_GAME)){

                    MsgCreateGame msg = (MsgCreateGame) receivedMessage;
                    msg.setClient(this);

                    GamesManager.getInstance().addMessageToQueue(msg);
                }else if(receivedMessage.getType().equals(MessageTypeC2S.JOIN_GAME)){

                    MsgJoinGame msg = (MsgJoinGame) receivedMessage;
                    msg.setClient(this);

                    GamesManager.getInstance().addMessageToQueue(msg);

                }else{
                    if(controller != null){
                        controller.addMessageToQueue(receivedMessage);
                    }
                }

            }
        } catch (EOFException e) {
            // Client has closed the connection
            System.out.println("Client disconnected.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            input.close();
            clientSocket.close();
        }
    }

    @Override
    public void sendMessage(MessageS2C message) {
        clientProxy.sendMessage(message);
    }

    @Override
    public void attachGameStub(VirtualStub gameStub) throws RemoteException {
        this.controller = gameStub.getController();
        System.out.println("Attached controller to socket");
    }


}
