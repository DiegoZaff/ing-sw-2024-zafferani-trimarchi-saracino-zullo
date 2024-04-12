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


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Coordinate other = (Coordinate) obj;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x,y);
    }
}
