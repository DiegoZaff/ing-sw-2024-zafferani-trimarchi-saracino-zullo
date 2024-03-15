package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.resources.Resource;

import java.util.ArrayList;
import java.util.Map;

public class Table {


    private Map<Coordinate, Cell> mapPositions;

    private ArrayList<Coordinate> playableCoords;
    /*
    * all'inizio solo 0,0 è playable, cardinitial verrà giocata sempre a 0,0
    * ad ogni carta piazzata l'array ottiene delle coordinate e ne perde altre
    * una coordinata unplayable non puo mai diventar eplayable
    */


    private ArrayList<Coordinate> unplayableCoords;

    /*
    * all'inizio non ci sono coordinate unplayable
    * ad ogni carta piazzata l'array ottiene nuove coordinate, non vengono miai cancellate le coordinate
    */

    private Map<Resource, Integer> resourceCounters;

    /*
    * inzialemnte è 0 per ogni risorsa, incomincia ad incrementarsi ad ogni carta piazzata
    * viene decrementato quando posiziono spora una carta con una risorsa sopra
    */


}
