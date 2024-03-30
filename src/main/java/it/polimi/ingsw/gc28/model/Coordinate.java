package it.polimi.ingsw.gc28.model;

public record Coordinate(int x, int y) {
    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}
