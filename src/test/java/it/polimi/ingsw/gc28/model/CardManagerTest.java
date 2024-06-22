package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.cards.*;
import org.junit.Test;
/**
 * this class tests CardsManager
 */
public class CardManagerTest {

    @Test
    public void test(){

        CardInitial cardInitial = CardsManager.getInstance().getCardInitialFromId("INI_1").get();
        CardResource cardResource = CardsManager.getInstance().getCardResourceFromId("RES_1").get();
        CardGold cardGold = CardsManager.getInstance().getCardGoldFromId("GOLD_1").get();
        CardObjective cardObjective = CardsManager.getInstance().getCardObjectiveFromId("OBJ_1").get();
        Card card = CardsManager.getInstance().getCardFromId("RES_26").get();


        System.out.println( cardInitial.toString(true) ) ;
        System.out.println( cardResource.toString(true) ) ;
        System.out.println( cardGold.toString(true) ) ;
        System.out.println( cardInitial.toString(false) ) ;
        System.out.println( cardResource.toString(false) ) ;
        System.out.println( cardGold.toString(false) ) ;
        System.out.println( card.toString() ) ;
        System.out.println( cardObjective.toString() ) ;









    }
}
