package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;

import java.io.IOException;
import java.util.ArrayList;

public class MsgOnGameStarted extends MessageS2C{

    ArrayList<Player> players;

    public MsgOnGameStarted(ArrayList<Player> players){
        this.players = players;
    }
    @Override
    public void update(VirtualView non_va_passata_una_VV_ma_una_GameRepresentation ) throws IOException {
    }
}
