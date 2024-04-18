package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;

import java.io.IOException;

public class MsgOnNextExpectedPlayerAction extends MessageS2C{

    ActionType actionType;
    String playerOfTurn;

    public MsgOnNextExpectedPlayerAction(ActionType actionType, String playerOfTurn){
        this.actionType = actionType;
        this.playerOfTurn = playerOfTurn;
    }
    @Override
    public void update(VirtualView non_va_passata_una_VV_ma_una_GameRepresentation ) throws IOException {

    }
}
