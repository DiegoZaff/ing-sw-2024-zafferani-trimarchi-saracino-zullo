package it.polimi.ingsw.gc28.model;


import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.cards.CardInitial;
import it.polimi.ingsw.gc28.model.cards.CardResource;
import it.polimi.ingsw.gc28.model.cards.CardGold;
import it.polimi.ingsw.gc28.model.challenge.utils.ChallengeType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourcePrimaryType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceSpecialType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

public class GoldCardPointsTest {


    private Player tester;
    int points;
    CardGame bridge = new CardResource("UNKNOWN_0", new ResourceType[]{ResourceType.noResource,ResourceType.noResource,ResourceType.noResource,ResourceType.noResource}, ResourcePrimaryType.MUSHROOM, 0);

    CardGame initialCard = new CardInitial("UNKNOWN_INIT", new ResourceType[]{ResourceType.noResource,ResourceType.noResource,ResourceType.noResource,ResourceType.noResource},
            new ResourceType[]{ResourceType.noResource,ResourceType.noResource,ResourceType.noResource,ResourceType.noResource},
            new ResourceType[]{ResourceType.noResource,ResourceType.noResource,ResourceType.noResource} );

    CardGame leafCard = new CardResource("UNKNOWN_1", new ResourceType[]{ResourceType.FEATHER, ResourceType.FEATHER, ResourceType.noResource, ResourceType.FOX},
            ResourcePrimaryType.LEAF, 0);


    CardGold goldFoxCard = new CardGold("UNKNOWN_4", new ResourceType[]{ResourceType.FEATHER, ResourceType.noResource, ResourceType.noResource, ResourceType.noResource},
            ResourcePrimaryType.FOX, 0, new ResourcePrimaryType[]{},
            ChallengeType.POINTS_PER_RESOURCE, ResourceSpecialType.FEATHER);


    CardGold goldMushroomCard = new CardGold("UNKNOWN_6", new ResourceType[]{ResourceType.noResource, ResourceType.noResource, ResourceType.noResource, ResourceType.noResource},
            ResourcePrimaryType.MUSHROOM, 0, new ResourcePrimaryType[]{},
            ChallengeType.POINTS_PER_COVER, ResourceSpecialType.noResource);

    CardGold goldLeafCard = new CardGold("UNKNOWN_7", new ResourceType[]{ResourceType.noResource, ResourceType.noResource, ResourceType.noResource, ResourceType.noResource},
            ResourcePrimaryType.LEAF, 5, new ResourcePrimaryType[]{},
            null, ResourceSpecialType.noResource);



    @BeforeEach
    public void initTestTable () throws Exception {

        tester = new Player("Tester");
        tester.playCard(initialCard, true , new Coordinate(0, 0));
        points = tester.getPoints();
        assertEquals(0, points, "error in initialization");
    }


    @Test
    public void Test1() throws Exception {

        tester.playCard(goldFoxCard, true, new Coordinate(1, -1));

        points = tester.getPoints();
        assertEquals(1, points, "Points from special resources on current card are calculated incorrectly");

    }
    @Test
    public void Test2() throws Exception {

        tester.playCard(leafCard, true, new Coordinate(1, -1));
        tester.playCard(goldFoxCard, true, new Coordinate(2, -2));

        points = tester.getPoints();
        assertEquals(3, points, "Points from special resources on multiple cards are calculated incorrectly");

    }

    @Test
    public void Test3() throws Exception {

        tester.playCard(goldMushroomCard, true, new Coordinate(1, -1));

        points = tester.getPoints();
        assertEquals(2, points, "Points from covered vertex are calculated incorrectly");

    }

    @Test
    public void Test4() throws Exception {

        tester.playCard(bridge, true, new Coordinate(1, -1));
        tester.playCard(bridge, true, new Coordinate(1, 1));
        tester.playCard(goldMushroomCard, true, new Coordinate(2, 0));

        points = tester.getPoints();
        assertEquals(4, points, "Points from 2 covered vertex are calculated incorrectly");

    }
    @Test
    public void Test5() throws Exception {

        tester.playCard(bridge, true, new Coordinate(1, -1));
        tester.playCard(bridge, true, new Coordinate(1, 1));
        tester.playCard(bridge, true, new Coordinate(2, 2));
        tester.playCard(bridge, true, new Coordinate(3, 1));
        tester.playCard(goldMushroomCard, true, new Coordinate(2, 0));

        points = tester.getPoints();
        assertEquals(6, points, "Points from 3 covered vertex are calculated incorrectly");

    }
    @Test
    public void Test6() throws Exception {

        tester.playCard(bridge, true, new Coordinate(1, -1));
        tester.playCard(bridge, true, new Coordinate(1, 1));
        tester.playCard(bridge, true, new Coordinate(2, 2));
        tester.playCard(bridge, true, new Coordinate(3, 1));
        tester.playCard(bridge, true, new Coordinate(-2, -2));
        tester.playCard(bridge, true, new Coordinate(3, -1));
        tester.playCard(goldMushroomCard, true, new Coordinate(2, 0));

        points = tester.getPoints();
        assertEquals(6, points, "Points from 4 covered vertex are calculated incorrectly");

    }


    @Test
    public void Test7() throws Exception {

        tester.playCard(goldLeafCard, true , new Coordinate(1, -1));

        points = tester.getPoints();
        assertEquals(5, points, "Points noChallenge are calculated incorrectly");

    }


}