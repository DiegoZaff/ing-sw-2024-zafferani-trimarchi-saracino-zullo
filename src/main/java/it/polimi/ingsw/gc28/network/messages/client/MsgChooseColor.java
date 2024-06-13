package it.polimi.ingsw.gc28.network.messages.client;

import it.polimi.ingsw.gc28.controller.GameController;

import java.rmi.RemoteException;

public class MsgChooseColor extends MessageC2S{
    private final String playerName;
    private final String color;
    public MsgChooseColor(String playerName, String color) {
        super(MessageTypeC2S.CHOOSE_COLOR);
        this.playerName = playerName;
        this.color = color;
    }
    @Override
    public void execute(GameController controller) throws RemoteException {
        controller.chooseColor(playerName, color);
    }
}
