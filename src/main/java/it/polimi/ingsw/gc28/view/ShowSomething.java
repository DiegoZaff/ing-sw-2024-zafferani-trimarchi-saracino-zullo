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

    public boolean showSomething (ArrayList<String> commandsList){

        String action = commandsList.getFirst();

        switch (action) {
            case "showHand" : {
                boolean isFront = true;
                if (commandsList.size() == 2) {
                    String orientation = commandsList.get(1);
                    if (orientation.equals("back"))
                    {
                        isFront = false;
                    }
                    else
                    {
                        System.out.println("You can only have 'back' as an argument\n");
                        return true;
                    }
                }
                GameManagerClient.getInstance().showHand(isFront);
                return true;
            }
            case "showCardInitial" : { //TODO:  show back initial?
                if (commandsList.size()!= 1) {
                    System.out.println("No parameters are needed after 'showCardInitial'\n");
                    return true;
                }
                GameManagerClient.getInstance().showCardInitial();
                return true;
            }
            case "showTable" : {
                if (commandsList.size() == 2) {
                    String player = commandsList.get(1); //TODO: check the player exists
                    GameManagerClient.getInstance().showTable(player);
                    return true;
                } else {
                    GameManagerClient.getInstance().showTable();
                    return true;
                }
            }
            case "showPoints" : {
                if (commandsList.size()!= 1) {
                    System.out.println("No parameters are needed after 'showPoints'\n");
                    return true;
                }
                GameManagerClient.getInstance().showPoints();
                return true;
            }

            case "showPlayerAndAction" : {
                if (commandsList.size()!= 1) {
                    System.out.println("No parameters are needed after 'showPlayerAndAction'\n");
                    return true;
                }
                GameManagerClient.getInstance().showPlayerAndAction();
                return true;
            }
            case "showDrawableCards" : {
                if (commandsList.size()!= 1) {
                    System.out.println("No parameters are needed after 'showDrawableCards'\n");
                    return true;
                }
                GameManagerClient.getInstance().showDrawableCards();
                return true;

            }
            case "showGlobalObjectives" : {
                if (commandsList.size()!= 1) {
                    System.out.println("No parameters are needed after 'showGlobalObjectives'\n");
                    return true;
                }
                GameManagerClient.getInstance().showGlobalObjectives();
                return true;

            }
            case "showObjective" :{
                if (commandsList.size()!= 1) {
                    System.out.println("No parameters are needed after 'showObjective'\n");
                    return true;
                }
                GameManagerClient.getInstance().showYourObjective();
                return true;

            }
            case "showObjectivesToChoose" : {
                if (commandsList.size()!= 1) {
                    System.out.println("No parameters are needed after 'showObjectivesToChoose'\n");
                    return true;
                }
                GameManagerClient.getInstance().showObjectivesToChoose();
                return true;

            }
            case "showChat" : {
                if (commandsList.size()!= 1) {
                    System.out.println("No parameters are needed after 'showChat'\n");
                    return true;
                }
                GameManagerClient.getInstance().showGlobalChat();
                return true;

            }
            case "showPrivateChat" : {
                if (commandsList.size() == 2) {
                    String player = commandsList.get(1);//TODO: check the player exists
                    GameManagerClient.getInstance().showPrivateChat(player);
                    return true;
                } else {
                    System.out.println("Too many arguments\n");
                    return true;
                }
            }
            case "?" :{
                System.out.println("command List:\n" +
                        "-showCardInitial: print your initial card  \n" +
                        "-showHand: print your hand, add 'back' to the command to print the cards' backs\n" +
                        "-showTable: print your table, add a player nickname to print his table\n" +
                        "-showPoints: print the players points\n" +
                        "-showPlayerAndAction: print the next expected move and who is due do play\n" +
                        "-showDrawableCards: print the current drawable card from all the decks\n" +
                        "-showGlobalObjectives: print the global objectives\n" +
                        "-showObjective: print your private objective\n" +
                        "-showObjectivesToChoose: print the objectives that you can choose\n" +
                        "-showChat: show the global chat\n" +
                        "-showPrivateChat: show the private chat, add a player nickname to print his table\n" +
                        "-joinGame gameId myNickname: join the game that has the selected gameId\n" +
                        "-createGame myNickname numberOfPlayers: create a new game\n" +
                        "-chooseObj OBJ_id: choose the selected objective\n" +
                        "-drawCard: draw a card, add goldDeck/resourceDeck to draw a random card from the selected deck or the cardId to draw the selected card\n" +
                        "-playCard cardId up/down x y: play the card at the specified coordinate\n");
                return true;
            }
            default: {
                return false;

            }

            }


        }
    }


