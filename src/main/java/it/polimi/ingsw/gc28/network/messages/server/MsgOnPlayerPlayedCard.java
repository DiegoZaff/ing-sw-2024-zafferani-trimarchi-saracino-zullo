package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.View.GameRepresentation;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Table;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;

import java.io.IOException;
import java.util.ArrayList;

public class MsgOnPlayerPlayedCard extends MessageS2C{

    String playerName;
    Table table;
    int newPlayerPoints;

    public MsgOnPlayerPlayedCard(String playerName, Table table, int newPlayerPoints){
        this.playerName = playerName;
        this.table = table;
        this.newPlayerPoints = newPlayerPoints;
    }

    @Override
    public void update(GameRepresentation gameRepresentation) throws IOException {

    }
}
