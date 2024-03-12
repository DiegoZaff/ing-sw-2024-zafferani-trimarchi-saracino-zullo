package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.resources.Resource;

import java.util.ArrayList;
import java.util.Map;

public class Table {


    private Map<Coordinate, Cell> mapPositions;

    private ArrayList<Coordinate> playableCoords;


    private ArrayList<Coordinate> unplayableCoords;

    private Map<Resource, Integer> resourceCounters;


}
