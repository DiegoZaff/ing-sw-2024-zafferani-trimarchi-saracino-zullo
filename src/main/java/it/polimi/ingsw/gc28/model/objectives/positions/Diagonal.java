package it.polimi.ingsw.gc28.model.objectives.positions;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.objectives.positions.utils.DiagonalType;

import java.io.Serializable;
import java.util.ArrayList;

public class Diagonal extends PositionType implements Serializable {

    public final DiagonalType diagonalType;

    public Diagonal(DiagonalType diagonalType) {
        this.diagonalType = diagonalType;
    }

    /**
     * this method is used to get the diagonal coordinates for each position type
     * @param coord starting coordinate
     * @return
     */
    @Override
    public ArrayList<Coordinate> getNeighborsCoordinates(Coordinate coord){
        ArrayList<Coordinate> coords = new ArrayList<Coordinate>();

        if(diagonalType == DiagonalType.MAIN_DIAGONAL){
            coords.add(new Coordinate(coord.x() - 1, coord.y() - 1));
            coords.add(coord);
            coords.add(new Coordinate(coord.x() + 1, coord.y() + 1));
        }else{
            coords.add(new Coordinate(coord.x() - 1, coord.y() + 1));
            coords.add(coord);
            coords.add(new Coordinate(coord.x() + 1, coord.y() -1));
        }
        return coords;

    }

    public String toString(String color1, String color2, String color3){
        return diagonalType.toString(color1, color2, color3);
    }

}
