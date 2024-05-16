package it.polimi.ingsw.gc28.view;

import java.util.ArrayList;

public class ShowSomething {

    private static ShowSomething instance;

    public static ShowSomething getInstance() {
        if (instance == null) {
            instance = new ShowSomething();
        }
        return instance;

    }

    public void showSomething (ArrayList<String> commandsList){

        String action = commandsList.getFirst();

        switch (action) {
            case "showHand" -> {
                boolean isFront = true;
                if (commandsList.size() == 2) {
                    String orientation = commandsList.get(1);
                    isFront = orientation.equals("up");
                }

                GameManagerClient.getInstance().showHand(isFront);
            }
            case "showCardInitial" -> GameManagerClient.getInstance().showCardInitial();
            case "showTable" -> {
                if (commandsList.size() == 2) {
                    String player = commandsList.get(1);

                    GameManagerClient.getInstance().showTable(player);

                } else {
                    GameManagerClient.getInstance().showTable();
                }
            }
            case "showPoints" -> GameManagerClient.getInstance().showPoints();
            case "showPlayerAndAction" -> GameManagerClient.getInstance().showPlayerAndAction();
            case "showDrawableCards" -> GameManagerClient.getInstance().showDrawableCards();
            case "showGlobalObjectives" -> GameManagerClient.getInstance().showGlobalObjectives();
            case "showObjective" -> GameManagerClient.getInstance().showYourObjective();
            case "showObjectivesToChoose" -> GameManagerClient.getInstance().showObjectivesToChoose();
            case "showChat" -> GameManagerClient.getInstance().showGlobalChat();
        }
    }

}
