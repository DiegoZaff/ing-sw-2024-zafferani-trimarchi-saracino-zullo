package it.polimi.ingsw.gc28.network.socket;


import it.polimi.ingsw.gc28.controller.GameController;
import it.polimi.ingsw.gc28.controller.GamesManager;
import it.polimi.ingsw.gc28.network.messages.client.*;
import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;
import it.polimi.ingsw.gc28.network.rmi.VirtualStub;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;

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
                System.out.printf("Received message %s from client %s%n", receivedMessage.getType(), clientSocket.getInetAddress());

                if(receivedMessage.getType().equals(MessageTypeC2S.CREATE_GAME)){

                    MsgCreateGame msg = (MsgCreateGame) receivedMessage;
                    msg.setClient(this);

                    GamesManager.getInstance().addMessageToQueue(msg);
                }else if(receivedMessage.getType().equals(MessageTypeC2S.JOIN_GAME)){

                    MsgJoinGame msg = (MsgJoinGame) receivedMessage;
                    msg.setClient(this);

                    GamesManager.getInstance().addMessageToQueue(msg);

                }else if(receivedMessage.getType().equals(MessageTypeC2S.JOINABLE_GAMES)){

                    MsgJoinableGames msg = (MsgJoinableGames) receivedMessage;
                    msg.setClient(this);

                    GamesManager.getInstance().addMessageToQueue(msg);

                }else if (receivedMessage.getType().equals(MessageTypeC2S.RECONNECT)){
                    MsgReconnect msg = (MsgReconnect) receivedMessage;
                    msg.setClient(this);

                    GamesManager.getInstance().addMessageToQueue(msg);
                }

                else{
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
    public void sendMessage(MessageS2C message) throws RemoteException {
        clientProxy.sendMessage(message);
    }

    @Override
    public void attachGameStub(VirtualStub gameStub) throws RemoteException {
        this.controller = gameStub.getController();
        this.server.addController(this, controller);
        System.out.println("Attached controller to socket");
    }

    @Override
    public void closeConnectionGame() throws RemoteException {
        this.controller = null;
    }
}
