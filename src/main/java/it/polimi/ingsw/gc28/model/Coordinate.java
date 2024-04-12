package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.resources.ResourcePrimary;

import java.util.Objects;

public record Coordinate(int x, int y) {
    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}
