package it.polimi.ingsw.gc28.model.objectives.positions;
import it.polimi.ingsw.gc28.model.Coordinate;
import java.util.ArrayList;


/**
 * This class generalizes the positions that cards can have in a position objective.
 */
public abstract class PositionType {

    /**
     * @param coord starting coordinate
     * @return and array of coordinates ordered from bottom left to top right according to the
     * pattern of the corresponding position.
     */
    public abstract ArrayList<Coordinate> getNeighborsCoordinates(Coordinate coord);

    public abstract String toString(String color1, String color2, String color3);
}
