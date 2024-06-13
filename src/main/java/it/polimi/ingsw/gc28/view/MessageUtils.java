package it.polimi.ingsw.gc28.view;

import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.chat.ChatMessage;
import it.polimi.ingsw.gc28.network.messages.client.*;
import it.polimi.ingsw.gc28.network.rmi.RmiClient;

import java.util.ArrayList;
import java.util.Optional;

public class MessageUtils {

    public static Optional<MessageC2S> createMessage(ArrayList<String> commandsList, RmiClient rmiClient, String gameId, String userName) {
        String action = commandsList.getFirst();
        String playerName;

        switch (action) {
            case "createGame" -> {
                if (commandsList.size() != 3) {
                    System.out.println("Invalid format in create game");
                    return Optional.empty();
                }
                playerName = commandsList.get(1);

                int nPlayers = 0;
                try {
                    nPlayers = Integer.parseInt(commandsList.get(2));
                } catch (NumberFormatException e) {
                    System.err.println(e.getMessage());
                    return Optional.empty();
                }

                if (nPlayers < 2 || nPlayers > 4) {
                    System.out.println("Invalid number of players");
                    return Optional.empty();
                }

                MsgCreateGame message =  new MsgCreateGame(null, playerName, nPlayers);

                return Optional.of(message);
            }
            case "joinGame" -> {
                if (commandsList.size() != 3) {
                    System.out.println("Invalid format in join game");
                    return Optional.empty();
                }

                String newGameId = commandsList.get(1);
                playerName = commandsList.get(2);

                MsgJoinGame message = new MsgJoinGame(newGameId, playerName);

                return Optional.of(message);
            }case "reconnect" -> {
                if (commandsList.size() != 3) {
                    System.out.println("Invalid format in reconnect");
                    return Optional.empty();
                }

                String newGameId = commandsList.get(1);
                playerName = commandsList.get(2);

                MsgReconnect message = new MsgReconnect(newGameId, playerName);

                return Optional.of(message);
            }
            case "chooseColor" -> {
                if (commandsList.size() != 2) {
                    System.out.println("Invalid format in choose color");
                    return Optional.empty();
                }

                String color = commandsList.get(1);

                MsgChooseColor message = new MsgChooseColor(userName, color);
                return Optional.of(message);
            }
            case "chooseObj" -> {
                if (commandsList.size() != 2) {
                    System.out.println("Invalid format in choose objective");
                    return Optional.empty();
                }

                String cardId = commandsList.get(1);

                MsgChooseObjective message = new MsgChooseObjective(userName, cardId);
                return Optional.of(message);
            }
            case "drawCard" -> {
                if (commandsList.size() != 2) {
                    System.out.println("Invalid format in drawCard");
                    return Optional.empty();
                }

                String arg = commandsList.get(1);

                MessageC2S message = null;

                if (arg.equals("goldDeck")) {
                    message = new MsgDrawGameCard(userName, gameId, true);
                } else if (arg.equals("resourceDeck")) {
                    message = new MsgDrawGameCard(userName, gameId, false);
                } else if (arg.startsWith("RES") || arg.startsWith("GOLD")) {
                    message = new MsgDrawnVisibleGameCard(userName, gameId, arg);
                } else {
                    System.out.println("Invalid format in drawCard");
                }

                return Optional.ofNullable(message);
            }
            case "playCard" -> {
                if (commandsList.size() != 5) {
                    System.out.println("Invalid format command playCard");
                    return Optional.empty();
                }

                String cardId = commandsList.get(1);
                String isFrontString = commandsList.get(2);

                if (!isFrontString.equals("up") && !isFrontString.equals("down")) {
                    System.out.println("Invalid isFront format in playCard command");
                    return Optional.empty();
                }
                boolean isFront = isFrontString.equals("up");

                Integer x = null;
                Integer y = null;

                try {
                    x = Integer.parseInt(commandsList.get(3));
                    y = Integer.parseInt(commandsList.get(4));
                } catch (NumberFormatException e) {
                    System.out.println(e.getMessage());
                    return Optional.empty();
                }

                MsgPlayGameCard message = new MsgPlayGameCard(userName, cardId, isFront, new Coordinate(x,y));
                return Optional.of(message);
            }
            case "sendChatMessage" -> {
                StringBuilder builder = new StringBuilder();
                commandsList.removeFirst();
                for (String str : commandsList) {
                    builder.append(str).append(" ");
                }

                if (!builder.isEmpty()) {
                    builder.deleteCharAt(builder.length() - 1);
                }
                ChatMessage chatMessage = new ChatMessage(builder.toString(), userName, "all",false);

                MsgChatMessage message = new MsgChatMessage(gameId, chatMessage);
                return Optional.of(message);
            }
            case "sendPrivateChatMessage" -> {
                StringBuilder builder = new StringBuilder();
                commandsList.removeFirst();
                String receiver = commandsList.getFirst();
                //TODO: verificare che esista un gicatore con tale nome.
                commandsList.removeFirst();

                for (String str : commandsList) {
                    builder.append(str).append(" ");
                }

                if (!builder.isEmpty()) {
                    builder.deleteCharAt(builder.length() - 1);
                }
                ChatMessage chatMessage = new ChatMessage(builder.toString(), userName, receiver, true);

                MsgChatMessage message = new MsgChatMessage(gameId, chatMessage);
                MsgChatMessage msgChatMessage = new MsgChatMessage(gameId, chatMessage);
                return Optional.of(msgChatMessage);
            }
            case "sendGames" -> {
                MsgJoinableGames msg = new MsgJoinableGames();

                return Optional.of(msg);


            }
            default -> {
                System.out.println("Invalid command");
                return Optional.empty();
            }
        }
    }

    public static boolean showSomething (ArrayList<String> commandsList){

        String action = commandsList.getFirst();

        switch (action) {
            case "showColor" : {
                if (commandsList.size()!= 1) {
                    System.out.println("No parameters are needed after 'showColor'\n");
                    return true;
                }
                GameManagerClient.getInstance().showColor();
                return true;
            }
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
            case "whoami" : {
                String playerName = GameManagerClient.getInstance().getPlayerName();
                System.out.printf("You are %s%n", playerName);
                return true;
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
                        "-chooseColor COLOR: choose the selected color\n" +
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
