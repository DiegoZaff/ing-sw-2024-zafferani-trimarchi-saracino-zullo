package it.polimi.ingsw.gc28.model;


import java.io.Serializable;

public record Coordinate(int x, int y) implements Serializable {
    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}
