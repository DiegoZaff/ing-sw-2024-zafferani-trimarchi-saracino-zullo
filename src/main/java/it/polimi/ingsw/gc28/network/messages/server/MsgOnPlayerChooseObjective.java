package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.network.rmi.VirtualView;

import java.io.IOException;

public class MsgOnPlayerChooseObjective extends MessageS2C{
    String playerName;
    String cardId;

    MsgOnPlayerChooseObjective(String playerName, String cardId){
        this.playerName = playerName;
        this.cardId = cardId;
    }

    @Override
    public void update(VirtualView non_va_passata_una_VV_ma_una_GameRepresentation ) throws IOException {

    }
}
