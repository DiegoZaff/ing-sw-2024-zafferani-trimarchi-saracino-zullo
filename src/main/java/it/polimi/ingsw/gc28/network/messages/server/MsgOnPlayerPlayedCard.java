package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Table;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;

import java.io.IOException;
import java.util.ArrayList;

public class MsgOnPlayerPlayedCard extends MessageS2C{

    String playerName;
    Table newTable;
    int newPlayerPoints;
    ArrayList<Coordinate> newPlayableCoords;
    MsgOnPlayerPlayedCard(String playerName, Table newTable, int newPlayerPoints, ArrayList<Coordinate> newPlayableCoords){
        this.playerName = playerName;
        this.newTable = newTable;
        this.newPlayerPoints = newPlayerPoints;
        this.newPlayableCoords = newPlayableCoords;
    }
    @Override
    public void update(VirtualView non_va_passata_una_VV_ma_una_GameRepresentation ) throws IOException {

    }
}
