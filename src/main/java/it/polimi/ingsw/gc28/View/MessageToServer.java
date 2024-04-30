package it.polimi.ingsw.gc28.View;

import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.network.messages.client.*;

import java.util.ArrayList;

public class MessageToServer {

    private static MessageToServer instance;
    ArrayList<String> commandsList;


    public static MessageToServer getInstance() {
        if (instance == null) {
            instance = new MessageToServer();
        }
        return instance;
    }
    /*public MessageC2S createMessage(){

        String action = commandsList.getFirst();
        String userName;

        switch (action) {
            case "createGame":
                if (commandsList.size() != 3) {
                    System.err.println("Invalid format");
                    break;
                }

                userName = commandsList.get(1);

                Integer nPlayers = null;
                try {
                    nPlayers = Integer.parseInt(commandsList.get(2));
                    //TODO: check that this number is between 2 and 4
                } catch (NumberFormatException e) {
                    System.err.println(e.getMessage());
                    break;
                }

                MsgCreateGame CGMess = new MsgCreateGame(gameId, userName, nPlayers, this );
                //qui non sono uniformati tcp e rmi non so come fare
                server.sendMessage(CGMess);

                break;
            case "joinGame":

                if (commandsList.size() != 3) {
                    System.err.println("Invalid format");
                    return;
                }

                String gameIdToJoin = commandsList.get(1);

                userName = commandsList.get(2);

                MsgJoinGame JGMess = new MsgJoinGame(this, gameIdToJoin, userName);
                server.sendMessage(JGMess);

                break;
            case "chooseObj": {

                if (commandsList.size() != 2) {
                    System.err.println("Invalid format");
                    return;
                }

                String cardId = commandsList.get(1);

                MsgChooseObjective COMess = new MsgChooseObjective(userName, gameId, cardId);
                server.sendMessage(COMess);

                break;
            }
            case "drawCard":

                if (commandsList.size() < 2) {
                    System.err.println("Invalid format");
                    return;
                }

                String arg = commandsList.get(1);



                if (arg.equals("goldDeck")) {
                    MsgDrawGameCard DGCMessGold = new MsgDrawGameCard( userName, gameId, true);
                    server.sendMessage(DGCMessGold);

                } else if (arg.equals("resourceDeck")) {
                    MsgDrawGameCard DGCMessRes = new MsgDrawGameCard( userName, gameId, false);
                    server.sendMessage(DGCMessRes);

                } else if (arg.startsWith("RES") || arg.startsWith("GOLD")) {
                    MsgDrawnVisibleGameCard DGCMessFU = new MsgDrawnVisibleGameCard( userName, gameId, arg);
                    server.sendMessage(DGCMessFU);

                } else {
                    System.err.println("Invalid format");
                }
                break;
            case "playCard": {

                if (commandsList.size() != 5) {
                    System.err.println("Invalid format");
                    return;
                }

                String cardId = commandsList.get(1);

                String isFrontString = commandsList.get(2);

                if (!isFrontString.equals("up") && !isFrontString.equals("down")) {
                    System.err.println("Invalid isFront format");
                    return;
                }

                boolean isFront = isFrontString.equals("up");

                Integer x = null;
                Integer y = null;

                try {
                    x = Integer.parseInt(commandsList.get(3));
                    y = Integer.parseInt(commandsList.get(4));
                } catch (NumberFormatException e) {
                    System.err.println(e.getMessage());
                    return;
                }


                MsgPlayGameCard PGCMess = new MsgPlayGameCard( userName, cardId, gameId, isFront, new Coordinate(x,y));
                server.sendMessage(PGCMess);

                break;
            }
            default:
                System.err.println("Invalid First Argument!");
                break;
        }
    }*/
}
