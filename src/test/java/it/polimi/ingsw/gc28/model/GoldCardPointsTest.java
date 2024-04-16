package it.polimi.ingsw.gc28.model;


import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.cards.CardResource;
import it.polimi.ingsw.gc28.model.cards.CardGold;
import it.polimi.ingsw.gc28.model.challenge.utils.ChallengeType;
import it.polimi.ingsw.gc28.model.resources.Resource;
import it.polimi.ingsw.gc28.model.resources.ResourcePrimary;
import it.polimi.ingsw.gc28.model.resources.ResourceSpecial;
import it.polimi.ingsw.gc28.model.resources.utils.ResourcePrimaryType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceSpecialType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceType;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class GoldCardPointsTest {

    private Player tester = new Player("pippo");




    private Table testTable;

    private Coordinate play;

    private Map<Coordinate, Cell> mapPositions;

    private Map<Resource, Integer> resourceCounters;

    @Test
    public void goldCardPointsTest() throws Exception {

        ResourcePrimary res = new ResourcePrimary(ResourcePrimaryType.LEAF);
        ResourcePrimary[] resources = new ResourcePrimary[]{res, res, res};


        CardGame bridge = new CardResource("UNKOWN_0", new ResourceType[]{ResourceType.noResource,ResourceType.noResource,ResourceType.noResource,ResourceType.noResource}, ResourcePrimaryType.MUSHROOM, 0);

        CardGame leafCard = new CardResource("UNKNOWN_1", new ResourceType[]{ResourceType.FEATHER, ResourceType.FOX, ResourceType.noResource, ResourceType.FOX},
                ResourcePrimaryType.LEAF,
                0);
        CardGame mmooshroomCard = new CardResource("UNKNOWN_2", new ResourceType[]{ResourceType.noResource, ResourceType.MUSHROOM, ResourceType.noResource, ResourceType.MUSHROOM},
                ResourcePrimaryType.MUSHROOM,
                0);


        CardGame leafCard2 = new CardResource("UNKNOWN_3", new ResourceType[]{ResourceType.FEATHER, ResourceType.FEATHER, ResourceType.noResource, ResourceType.noResource},
                ResourcePrimaryType.LEAF,
                0);

        CardGold goldFoxCard = new CardGold("UNKNOWN_4", new ResourceType[]{ResourceType.FEATHER, ResourceType.FEATHER, ResourceType.noResource, ResourceType.noResource},
                ResourcePrimaryType.FOX, 0, new ResourcePrimaryType[]{},
                ChallengeType.POINTS_PER_RESOURCE, ResourceSpecialType.FEATHER);

        CardGold goldFoxCard2 = new CardGold("UNKNOWN_5", new ResourceType[]{ResourceType.noResource, ResourceType.noResource, ResourceType.noResource, ResourceType.noResource},
                ResourcePrimaryType.FOX, 2, new ResourcePrimaryType[]{},
                ChallengeType.POINTS_PER_RESOURCE, ResourceSpecialType.FEATHER);

        CardGold goldMushroomCard = new CardGold("UNKNOWN_6", new ResourceType[]{ResourceType.noResource, ResourceType.noResource, ResourceType.noResource, ResourceType.noResource},
                ResourcePrimaryType.MUSHROOM, 0, new ResourcePrimaryType[]{},
                ChallengeType.POINTS_PER_COVER, ResourceSpecialType.noResource);

        CardGold goldLeafCard = new CardGold("UNKNOWN_7", new ResourceType[]{ResourceType.noResource, ResourceType.noResource, ResourceType.noResource, ResourceType.noResource},
                ResourcePrimaryType.LEAF, 3, new ResourcePrimaryType[]{},
                null, ResourceSpecialType.noResource);

        CardGame Position = new CardGold("UNKNOWN_P", new ResourceType[]{ResourceType.noResource,ResourceType.noResource,ResourceType.noResource,ResourceType.noResource}, ResourcePrimaryType.MUSHROOM, 0, new ResourcePrimaryType[]{}, ChallengeType.POINTS_PER_COVER, null);
        /*
        tester.playCard(leafCard, true , new Coordinate(0, 0));
        int points = tester.getPoints();

        assertEquals(0, points, "Card is played correctly");



        tester.playCard(goldFoxCard, true , new Coordinate(1, -1));

        points = tester.getPoints();
        assertEquals(1, points, "Points from special resources are calculated correctly");
        */

        /* test 2
        tester.playCard(leafCard2, true , new Coordinate(1, -1));
        tester.playCard(goldFoxCard, true , new Coordinate(2, -2));

        points = tester.getPoints();

        assertEquals(3 points, "Points from multiple special resources are calculated correctly");

         */


        /*
        // test 3  requisiti non soddisfatti


         tester.playCard(goldFoxCard2, true, new Coordinate( 1, -1));
         */


         /* test 4

        tester.playCard(mmooshroomCard, true , new Coordinate(1, -1));
        tester.playCard(goldMushroomCard, true , new Coordinate(2, -2));

        points = tester.getPoints();

        assertEquals(2, points, "Points from covered vertex are calculated correctly");
        */


        /*
        // test 5

        tester.playCard(mmooshroomCard, true , new Coordinate(1, -1));
        tester.playCard(mmooshroomCard, true , new Coordinate(1, 1));

        tester.playCard(goldMushroomCard, true , new Coordinate(2, 0));

        points = tester.getPoints();

        assertEquals(4, points, "Points per multiple covered vertex are calculated correctly");

        */

        /*
        // test 6


        tester.playCard(mmooshroomCard, true , new Coordinate(1, -1));
        tester.playCard(goldLeafCard, true , new Coordinate(2, -2));

        points = tester.getPoints();

        assertEquals(3, points, "Points noChallenge are calculated correctly");
        */


        /* Test Position
        tester.playCard(bridge, true, new Coordinate(0,0));

        tester.playCard(bridge,true, new Coordinate(1,1));

        tester.playCard(bridge,true, new Coordinate(-1,1));


        tester.playCard(bridge,true, new Coordinate(2,2));

        tester.playCard(bridge,true, new Coordinate(1,3));

        tester.playCard(bridge,true, new Coordinate(-2,2));

        tester.playCard(bridge,true, new Coordinate(-1,3));



        tester.playCard(Position,true, new Coordinate(0,2));




        assertEquals(8, tester.getPoints(), "Points noChallenge are calculated correctly");

        */








    }


}
