package it.polimi.ingsw.gc28;

import it.polimi.ingsw.gc28.model.Cell;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.cards.CardResource;
import it.polimi.ingsw.gc28.model.objectives.Objective;
import it.polimi.ingsw.gc28.model.objectives.ObjectivePosition;
import it.polimi.ingsw.gc28.model.objectives.ObjectiveResources;
import it.polimi.ingsw.gc28.model.objectives.positions.Diagonal;
import it.polimi.ingsw.gc28.model.objectives.positions.PositionType;
import it.polimi.ingsw.gc28.model.objectives.positions.utils.DiagonalType;
import it.polimi.ingsw.gc28.model.resources.Resource;
import it.polimi.ingsw.gc28.model.resources.ResourcePrimary;
import it.polimi.ingsw.gc28.model.resources.ResourceSpecial;
import it.polimi.ingsw.gc28.model.resources.utils.ResourcePrimaryType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceSpecialType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is for unit-testing the different kinds of Objective, namely ObjectivePosition
 * ObjectiveResource and making sure points are calculated correctly given proper mapPositions
 * and resourceCounters.
 */
public class ObjectiveTest {
    private Objective objective;

    private Map<Coordinate, Cell> mapPositions;

    private Map<Resource, Integer> resourceCounters;


    /**
     * This test checks the behaviour of objectivePosition's calculatePoints.
     * A simple table is set up consisting of three cards of the correct resource
     * positioned diagonally.
     */
    @Test
    public void objectivePositionDiagonal_calculatePoints(){
        ResourcePrimary res = new ResourcePrimary(ResourcePrimaryType.LEAF);
        ResourcePrimary[] resources = new ResourcePrimary[]{res, res, res};
        PositionType positionType = new Diagonal(DiagonalType.MAIN_DIAGONAL);

        this.objective = new ObjectivePosition(positionType, 3,  resources);

        CardGame leafCard = new CardResource(new ResourceType[]{ResourceType.FEATHER, ResourceType.FOX, ResourceType.noResource, ResourceType.FOX},
                ResourcePrimaryType.LEAF,
                0);
        CardGame foxCard = new CardResource(new ResourceType[]{ResourceType.FEATHER, ResourceType.FOX, ResourceType.noResource, ResourceType.FOX},
                ResourcePrimaryType.FOX,
                0);

        // Simple case with a diagonal of three cards
        this.mapPositions = new HashMap<>(){{
            put(new Coordinate(0,0), new Cell(leafCard, 0, true));
            put(new Coordinate(1,1), new Cell(leafCard, 1, true));
            put(new Coordinate(2,2), new Cell(leafCard, 2, true));
        }};

        int points = objective.calculatePoints(this.mapPositions, this.resourceCounters);

        assertEquals(3, points , "Case: diagonal of 3 cards");

        // Edge Case, make sure that a diagonal of 4 cards doesn't give more points.
        this.mapPositions = new HashMap<>(){{
            put(new Coordinate(0,0), new Cell(leafCard, 0, true));
            put(new Coordinate(1,1), new Cell(leafCard, 1, true));
            put(new Coordinate(2,2), new Cell(leafCard, 2, true));
            put(new Coordinate(3,3), new Cell(leafCard, 3, true));
        }};

        points = objective.calculatePoints(this.mapPositions, this.resourceCounters);

        assertEquals(3, points, "Edge case: diagonal of 4 cards");

        // Diagonal of 6 cards, should give double points.
        this.mapPositions = new HashMap<>(){{
            put(new Coordinate(0,0), new Cell(leafCard, 0, true));
            put(new Coordinate(1,1), new Cell(leafCard, 1, true));
            put(new Coordinate(2,2), new Cell(leafCard, 2, true));
            put(new Coordinate(3,3), new Cell(leafCard, 3, true));
            put(new Coordinate(4,4), new Cell(leafCard, 4, true));
            put(new Coordinate(5,5), new Cell(leafCard, 5, true));
        }};

        points = objective.calculatePoints(this.mapPositions, this.resourceCounters);

        assertEquals(6, points, "Case: diagonal of 6 cards");

        // 1 Diagonal of 3 leaves, 1 diagonal of Foxes, 1 diagonal of Leaves and Foxes
        // Therefore only one is correct.
        this.mapPositions = new HashMap<>(){{
            put(new Coordinate(0,0), new Cell(leafCard, 0, true));
            put(new Coordinate(1,1), new Cell(leafCard, 1, true));
            put(new Coordinate(2,2), new Cell(leafCard, 2, true));
            put(new Coordinate(1,-1), new Cell(foxCard, 3, true));
            put(new Coordinate(2,0), new Cell(foxCard, 4, true));
            put(new Coordinate(3,1), new Cell(foxCard, 5, true));
            put(new Coordinate(2,-2), new Cell(leafCard, 3, true));
            put(new Coordinate(3,-1), new Cell(leafCard, 4, true));
            put(new Coordinate(4,0), new Cell(foxCard, 5, true));
        }};

        points = objective.calculatePoints(this.mapPositions, this.resourceCounters);

        assertEquals(3, points, "Case: multiple diagonals but only one is correct");
    }


    /**
     * This test checks the behaviour of objectiveResource's calculatePoints.
     * A resourceCounters is set up with 7 Potions. The objective requires 2 points to
     * score 2 points.
     */
    @Test
    public void objectiveResources_calculatePoints(){
        // Only one needed resource
        Map<Resource, Integer> resourcesNeeded = new HashMap<>(){{
            put(new ResourcePrimary(ResourcePrimaryType.LEAF), 2);
        }};
        this.objective = new ObjectiveResources(resourcesNeeded, 2);
        this.resourceCounters = new HashMap<>(){{
            put(new ResourcePrimary(ResourcePrimaryType.LEAF), 7);
            put(new ResourcePrimary(ResourcePrimaryType.FOX), 3);
            put(new ResourceSpecial(ResourceSpecialType.FEATHER), 5);
            put(new ResourceSpecial(ResourceSpecialType.PARCHMENT), 4);
        }};

        int points = this.objective.calculatePoints(mapPositions, resourceCounters);

        assertEquals(6, points, "Case: same resource");

        // Different resources
        resourcesNeeded = new HashMap<>(){{
            put(new ResourceSpecial(ResourceSpecialType.POTION), 2);
            put(new ResourceSpecial(ResourceSpecialType.FEATHER), 2);
            put(new ResourceSpecial(ResourceSpecialType.PARCHMENT), 2);
        }};
        this.objective = new ObjectiveResources(resourcesNeeded, 3);
        this.resourceCounters = new HashMap<>(){{
            put(new ResourceSpecial(ResourceSpecialType.POTION), 7);
            put(new ResourceSpecial(ResourceSpecialType.FEATHER), 5);
            put(new ResourceSpecial(ResourceSpecialType.PARCHMENT), 4);

        }};

        points = this.objective.calculatePoints(mapPositions, resourceCounters);

        assertEquals(6, points, "Case: different resources");
    }
}