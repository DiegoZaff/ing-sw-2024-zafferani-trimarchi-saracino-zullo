package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.cards.*;
import it.polimi.ingsw.gc28.model.challenge.utils.ChallengeType;
import it.polimi.ingsw.gc28.model.objectives.ObjectivePosition;
import it.polimi.ingsw.gc28.model.objectives.positions.Diagonal;
import it.polimi.ingsw.gc28.model.objectives.positions.PositionType;
import it.polimi.ingsw.gc28.model.objectives.positions.utils.DiagonalType;
import it.polimi.ingsw.gc28.model.objectives.positions.utils.GeneralPositionType;
import it.polimi.ingsw.gc28.model.resources.ResourcePrimary;
import it.polimi.ingsw.gc28.model.resources.utils.ResourcePrimaryType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceSpecialType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceType;
import org.junit.Test;

import java.util.HashMap;

public class TableTest {

    private String im1;
    private String im2;
    @Test
    public void showMyTable() {


        CardGame leafCard = new CardResource("UNKNOWN_1", new ResourceType[]{ResourceType.FEATHER, ResourceType.FOX, ResourceType.noResource, ResourceType.PARCHMENT},
                ResourcePrimaryType.LEAF,
                0, im1, im2);
        CardGold goldFoxCard = new CardGold("UNKNOWN_4", new ResourceType[]{ResourceType.BUTTERFLY, ResourceType.noResource, ResourceType.noResource, ResourceType.noResource},
                ResourcePrimaryType.FOX, 0, new ResourcePrimaryType[]{},
                ChallengeType.POINTS_PER_RESOURCE, ResourceSpecialType.FEATHER, im1, im2);

        Table table1 = new Table();

        table1.addMapPosition(new Coordinate(0, 0), new Cell(leafCard, 0, true));
        table1.addMapPosition(new Coordinate(1, 1), new Cell(leafCard, 0, true));
        table1.addMapPosition(new Coordinate(2, 2), new Cell(leafCard, 0, true));
        table1.addMapPosition(new Coordinate(3, 3), new Cell(leafCard, 0, true));
        table1.addMapPosition(new Coordinate(-1, 1), new Cell(goldFoxCard, 0, true));
        table1.addMapPosition(new Coordinate(0, 2), new Cell(leafCard, 0, true));
        table1.addMapPosition(new Coordinate(-2, 2), new Cell(leafCard, 0, true));
        table1.addMapPosition(new Coordinate(-1, -1), new Cell(goldFoxCard, 0, true));

        System.out.println(table1.toString());

        Player p1 = new Player("aaaaa");
        Player p2 = new Player("bbbbb");
        Player p3 = new Player("ccccc");

        ResourcePrimary res = new ResourcePrimary(ResourcePrimaryType.LEAF);
        ResourceType[] resources = new ResourceType[]{ResourceType.FOX};
        ResourceType[] resources4 = new ResourceType[]{ResourceType.BUTTERFLY, ResourceType.LEAF};
        ResourceType[] resources5 = new ResourceType[]{ResourceType.PARCHMENT};
        GeneralPositionType generalPositionType;
        GeneralPositionType generalPositionType2;
        generalPositionType = GeneralPositionType.MAIN_DIAGONAL;
        generalPositionType2 = GeneralPositionType.S_E_STACK;
        ResourcePrimaryType[] resources2 = new ResourcePrimaryType[]{ ResourcePrimaryType.FOX, ResourcePrimaryType.FOX, ResourcePrimaryType.FOX};


        CardObjective cardObjective = new CardObjective("id", 0, resources, im1, im2);
        CardObjective cardObjective2 = new CardObjective("id", generalPositionType, 0, resources2, im2, im1 );
        CardObjective cardObjective3 = new CardObjective("id", generalPositionType2, 2, resources2, im2, im1 );

        p1.setObjectiveChosen(cardObjective);

        System.out.println( p1.getObjectiveChosen().toString());

        p2.setObjectiveChosen(cardObjective2);
        System.out.println( p2.getObjectiveChosen().toString());

        p3.setObjectiveChosen(cardObjective3);
        System.out.println( p3.getObjectiveChosen().toString());

        CardInitial cardInitial = new CardInitial( "id", resources, resources4, resources5, im1, im2);
        p1.setCardInitial(cardInitial);
        System.out.println( p1.getCardInitial().toString()); //non viene mostrata
    }

}
