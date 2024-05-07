package it.polimi.ingsw.gc28.View;

import it.polimi.ingsw.gc28.model.Coordinate;
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

    public Optional<MessageC2S> createMessage(ArrayList<String> commandsList, RmiClient rmiClient) {
        String action = commandsList.getFirst();
        String userName;

        switch (action) {
            case "createGame" -> {
                if (commandsList.size() != 3) {
                    System.err.println("Invalid format");
                    return Optional.empty();
                }
                userName = commandsList.get(1);

                int nPlayers = 0;
                try {
                    nPlayers = Integer.parseInt(commandsList.get(2));
                } catch (NumberFormatException e) {
                    System.err.println(e.getMessage());
                    return Optional.empty();
                }

                if (nPlayers < 2 || nPlayers > 4) {
                    System.err.println("Invalid number of players");
                    return Optional.empty();
                }

                MsgCreateGame message;
                if (rmiClient == null) {
                    message = new MsgCreateGame(null, userName, nPlayers, null);
                } else {
                    message = new MsgCreateGame(null, userName, nPlayers, rmiClient);
                }
                return Optional.of(message);
            }
            case "joinGame" -> {
                if (commandsList.size() != 3) {
                    System.err.println("Invalid format");
                    return Optional.empty();
                }

                String gameId = commandsList.get(1);
                userName = commandsList.get(2);

                MsgJoinGame message;
                if (rmiClient == null) {
                    message = new MsgJoinGame(null, gameId, userName);
                } else {
                    message = new MsgJoinGame(rmiClient, gameId, userName);
                }
                return Optional.of(message);
            }
            case "chooseObj" -> {
                if (commandsList.size() != 4) {
                    System.err.println("Invalid format");
                    return Optional.empty();
                }

                String gameId = commandsList.get(1);
                userName = commandsList.get(2);
                String cardId = commandsList.get(3);

                MsgChooseObjective message = new MsgChooseObjective(userName, gameId, cardId);
                return Optional.of(message);
            }
            case "drawCard" -> {
                if (commandsList.size() != 4) {
                    System.err.println("Invalid format");
                    return Optional.empty();
                }

                String gameId = commandsList.get(1);
                userName = commandsList.get(2);
                String arg = commandsList.get(3);

                MessageC2S message = null;

                if (arg.equals("goldDeck")) {
                    message = new MsgDrawGameCard(userName, gameId, true);
                } else if (arg.equals("resourceDeck")) {
                    message = new MsgDrawGameCard(userName, gameId, false);
                } else if (arg.startsWith("RES") || arg.startsWith("GOLD")) {
                    message = new MsgDrawnVisibleGameCard(userName, gameId, arg);
                } else {
                    System.err.println("Invalid format");
                }

                return Optional.of(message);
            }
            case "playCard" -> {
                if (commandsList.size() != 7) {
                    System.err.println("Invalid format");
                    return Optional.empty();
                }

                String gameId = commandsList.get(1);
                userName = commandsList.get(2);
                String cardId = commandsList.get(3);
                String isFrontString = commandsList.get(4);

                if (!isFrontString.equals("up") && !isFrontString.equals("down")) {
                    System.err.println("Invalid isFront format");
                    return Optional.empty();
                }
                boolean isFront = isFrontString.equals("up");

                Integer x = null;
                Integer y = null;

                try {
                    x = Integer.parseInt(commandsList.get(5));
                    y = Integer.parseInt(commandsList.get(6));
                } catch (NumberFormatException e) {
                    System.err.println(e.getMessage());
                    return Optional.empty();
                }

                MsgPlayGameCard message = new MsgPlayGameCard(userName, cardId, gameId, isFront, new Coordinate(x,y));
                return Optional.of(message);
            }
            default -> {
                System.err.println("Invalid First Argument!");
                return Optional.empty();
            }
        }
    }
}
