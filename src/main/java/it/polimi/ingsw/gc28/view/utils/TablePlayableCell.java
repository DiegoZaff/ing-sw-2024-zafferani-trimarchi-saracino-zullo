package it.polimi.ingsw.gc28.view.utils;

import it.polimi.ingsw.gc28.model.Coordinate;

/**
 * Helper class to associate float position in the ui to the integer coordinate of the table
 */
public record TablePlayableCell(Coordinate coord, double rightDistance, double bottomDistance) {
}
