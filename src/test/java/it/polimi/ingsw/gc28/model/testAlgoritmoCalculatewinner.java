package it.polimi.ingsw.gc28.model;

import org.junit.Test;

import java.util.ArrayList;

public class testAlgoritmoCalculatewinner {




    @Test   //è una schifezza testa solo l'algorimto, eliminare qunado si vuole
    public void Test (){

        ArrayList<Player> players = new ArrayList<>();

        Player tester1 = new Player("t1");
        Player tester2 = new Player("t2");

        /*
        tester1.setPoints(0);
        tester2.setPoints(0);
        tester1.setObjectrivePoints(0);
        tester2.setObjectrivePoints(0);
        */
        players.add(tester1);
        players.add(tester2);




        int maxPoints = 0;
        ArrayList<Player> winners = new ArrayList<>();

        for (Player player : players ) {
            if (player.getPoints() > maxPoints) {
                maxPoints = player.getPoints();
                winners.clear();
                winners.add(player);
            } else if (player.getPoints() == maxPoints) {
                winners.add(player);
            }
        }

        ArrayList<Player> winnersAfterObjectivePointsCheck = new ArrayList<>();
        if (winners.size() > 1)
        {
            int maxObjectivePoints = 0;
            for (Player player : winners)
            {
                if (player.getObjectivePoints() > maxObjectivePoints)
                {
                    maxObjectivePoints = player.getObjectivePoints();
                    winnersAfterObjectivePointsCheck.clear();
                    winnersAfterObjectivePointsCheck.add(player);
                }
                else if (player.getObjectivePoints() == maxObjectivePoints)
                {
                    winnersAfterObjectivePointsCheck.add(player);
                }
            }
        }else{
            winnersAfterObjectivePointsCheck.add(winners.getFirst());
        }

        for (Player player : winnersAfterObjectivePointsCheck)
        {
            player.setWinner();
        }


        for (Player player : players ) {
            if (player.isWinner()){
                System.out.print(player.getName());
            }
        }







    }
}
