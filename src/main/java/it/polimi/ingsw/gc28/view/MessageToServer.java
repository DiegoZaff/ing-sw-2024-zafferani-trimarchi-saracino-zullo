package it.polimi.ingsw.gc28.view;

import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.chat.ChatMessage;
import it.polimi.ingsw.gc28.network.messages.client.*;
import it.polimi.ingsw.gc28.network.rmi.RmiClient;

import java.util.ArrayList;
import java.util.Optional;

public class MessageToServer {
    private static MessageToServer instance;

    public static MessageToServer getInstance() {
        if (instance == null) {
            instance = new MessageToServer();
        }
        return instance;
    }

    public Optional<MessageC2S> createMessage(ArrayList<String> commandsList, RmiClient rmiClient, String gameId, String userName) {
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

                MsgCreateGame message;
                if (rmiClient == null) {
                    message = new MsgCreateGame(null, playerName, nPlayers, null);
                } else {
                    message = new MsgCreateGame(null, playerName, nPlayers, rmiClient);
                }
                return Optional.of(message);
            }
            case "joinGame" -> {
                if (commandsList.size() != 3) {
                    System.out.println("Invalid format in join game");
                    return Optional.empty();
                }

                String newGameId = commandsList.get(1);
                playerName = commandsList.get(2);

                MsgJoinGame message;
                if (rmiClient == null) {
                    message = new MsgJoinGame(null, newGameId, playerName);
                } else {
                    message = new MsgJoinGame(rmiClient, newGameId, playerName);
                }
                return Optional.of(message);
            }
            case "chooseObj" -> {
                if (commandsList.size() != 2) {
                    System.out.println("Invalid format in choose objective");
                    return Optional.empty();
                }

                String cardId = commandsList.get(1);

                MsgChooseObjective message = new MsgChooseObjective(userName, gameId, cardId);
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

                MsgPlayGameCard message = new MsgPlayGameCard(userName, cardId, gameId, isFront, new Coordinate(x,y));
                return Optional.of(message);
            }
            case "chatMessage" -> {
                StringBuilder builder = new StringBuilder();
                commandsList.removeFirst();
                for (String str : commandsList) {
                    builder.append(str).append(" ");
                }

                if (!builder.isEmpty()) {
                    builder.deleteCharAt(builder.length() - 1);
                }
                ChatMessage chatMessage = new ChatMessage(builder.toString(), userName);

                MsgChatMessage message = new MsgChatMessage(gameId, chatMessage);
                return Optional.of(message);
            }
            default -> {
                System.out.println("Invalid command");
                return Optional.empty();
            }
        }
    }
}
