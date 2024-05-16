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
            case "?" -> System.out.println("command List:\n" +
                        "-showCardInitial: print your initial card  \n" +
                        "-showHand: print your hand, add 'down' to the command to print the cards face down\n" +
                        "-showTable: print your table, add a player nickname to print his table\n" +
                        "-showPoints: print the players points\n" +
                        "-showPlayerAndAction: print the next expected move and who is due do play\n" +
                        "-showDrawableCards: print the current drawable card from all the decks\n" +
                        "-showGlobalObjectives: print the global objectives\n" +
                        "-showObjective: print your private objective\n" +
                        "-showObjectivesToChoose: print the objectives that you can choose\n" +
                        "-showChat: show the global chat\n " +
                        "-joinGame gameId myNickname: join the game that has the selected gameId\n " +
                        "-createGame myNickname numberOfPlayers: create a new game\n " +
                        "-chooseObj OBJ_id: choose the selected objective\n " +
                        "-drawCard: draw a card, add goldDeck/resourceDeck to draw a random card from the selected deck or the cardId to draw the selected card\n" +
                        "-playCard cardId up/down x y: play the card at the specified coordinate\n" +
                        "-chat, da implementare");

            }
        }
    }


