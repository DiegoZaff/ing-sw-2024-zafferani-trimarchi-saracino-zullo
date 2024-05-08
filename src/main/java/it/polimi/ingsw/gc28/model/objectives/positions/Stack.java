package it.polimi.ingsw.gc28.model.objectives.positions;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.objectives.positions.utils.PositionStackType;

import java.io.Serializable;
import java.util.ArrayList;

public class Stack extends PositionType implements Serializable {
    public Stack(PositionStackType type) {
        this.type = type;
    }

    public final PositionStackType type;

    @Override
    public ArrayList<Coordinate> getNeighborsCoordinates(Coordinate coord) {
        ArrayList<Coordinate> coords = new ArrayList<Coordinate>();

        if (type == PositionStackType.S_E_STACK){
            coords.add(coord);
            coords.add(new Coordinate(coord.x(), coord.y() + 2));
            coords.add(new Coordinate(coord.x() + 1, coord.y() - 1));
        }else if (type == PositionStackType.S_W_STACK){
            coords.add(new Coordinate(coord.x() - 1, coord.y() - 1));
            coords.add(coord);
            coords.add(new Coordinate(coord.x(), coord.y() + 2));
        }else if (type == PositionStackType.N_E_STACK){
            coords.add(new Coordinate(coord.x(), coord.y() - 2));
            coords.add(coord);
            coords.add(new Coordinate(coord.x() + 1, coord.y() + 1));
        }else if (type == PositionStackType.N_W_STACK){
            coords.add(new Coordinate(coord.x() - 1, coord.y() + 1));
            coords.add(new Coordinate(coord.x(), coord.y() - 2));
            coords.add(coord);
        }

        return coords;
    }


}
