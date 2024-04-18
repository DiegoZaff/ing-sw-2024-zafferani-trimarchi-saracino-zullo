package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.network.rmi.VirtualView;

import java.io.IOException;

public class MsgReportMessage extends MessageS2C{

    String details;
     public MsgReportMessage(String details){
         this.details = details;
     }

    @Override
    public void update(VirtualView non_va_passata_una_VV_ma_una_GameRepresentation ) throws IOException {

    }
}
