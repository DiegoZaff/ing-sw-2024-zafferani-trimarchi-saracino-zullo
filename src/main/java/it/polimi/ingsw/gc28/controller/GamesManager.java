package it.polimi.ingsw.gc28.controller;

import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.network.messages.client.MessageC2S;
import it.polimi.ingsw.gc28.network.messages.client.MsgCreateGame;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class GamesManager {

    private static GamesManager instance;

    // TODO : change this so that it also stores clients connected to the game (sockets and virtual clients (RMI))
    private final Map<String, GameController> mapGames;

    private GamesManager() {
        mapGames = new HashMap<>();
    }

    public static GamesManager getInstance() {
        if (instance == null) {
            instance = new GamesManager();
        }
        return instance;
    }

    /**
     * This method is used when the client choose a network working with sockets.
     * The method receives a message from the client and call a controller's method to execute the client's request.
     * @param message is the message coming from the client.
     * @throws IOException
     */
    public void executeClientMessage(MessageC2S message) throws IOException {
        Optional<String> gameId = message.getGameId();
        // in base al messaggio che arriva, leggo i parametri e chiamo i metodi sotto
        if(gameId.isEmpty()){
            //case the message is a MsgCreateGame
            MsgCreateGame messageCreateGame = (MsgCreateGame) message;
            int nPlayers = messageCreateGame.getNumberOfPlayers();
            GameController newController = new GameController(new Game(nPlayers));
            String newGameId = newController.gameModel.getGameId();
            mapGames.put(newGameId, newController);
            message.execute(newController);
        }
        else {
            //case gameId is present
            GameController gameController = mapGames.get(gameId);
            message.execute(gameController);
        }
    }

    public void createGame(VirtualView client, String playerName, int numberOfPlayers)  {
        GameController newController = null;
        try {
            newController = new GameController(new Game(numberOfPlayers));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try{
            newController.addPlayerToGame(playerName, client);
        }catch (RuntimeException e){
            System.err.println("Error in game creation.");
            return;
        }

        String gameId = UUID.randomUUID().toString();
        mapGames.put(gameId, newController);

        try {
            client.onGameCreated(gameId, playerName, numberOfPlayers);
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
        }
    }


    public void joinGame(VirtualView client, String gameId, String userName){
        Optional<GameController> controller = getGameController(gameId);

        if(controller.isEmpty()){
            try {
                client.reportError("This game doesn't exist!");
                return;
            } catch (RemoteException e) {
                System.err.println(e.getMessage());
                return;
            }
        }

        try {
            controller.get().addPlayerToGame(userName, client);
        }catch (RuntimeException e){
            System.err.println(e.getMessage());
        }

    }

    public void playGameCard(String playerName, String gameId, String cardId, boolean isFront, Coordinate coordinate){
        Optional<GameController> controller = getGameController(gameId);

        if(controller.isEmpty()){
            System.err.println("PlayCard requested from non-existent game");
            return;
        }

        controller.get().playCard(playerName, cardId, isFront, coordinate);

    }

    public void drawCard(String playerName, String gameId, boolean fromGoldDeck){
        Optional<GameController> controller = getGameController(gameId);

        if(controller.isEmpty()){
            System.err.println("DrawCard from decks requested from non-existent game");
            return;
        }

        controller.get().drawCard(playerName, fromGoldDeck);
    }

    public void drawCard(String playerName, String gameId, String cardId){
        Optional<GameController> controller = getGameController(gameId);

        if(controller.isEmpty()){
            System.err.println("DrawCard form visible cards requested from non-existent game");
            return;
        }

        controller.get().drawCard(playerName, cardId);
    }

    public void chooseObjective(String playerName, String gameId, String cardId){
        Optional<GameController> controller = getGameController(gameId);

        if(controller.isEmpty()){
            System.err.println("ChooseObjective requested from non-existent game");
            return;
        }

        controller.get().chooseObjectivePersonal(playerName, cardId);
    }

    public Optional<GameController> getGameController(String id){
        GameController controller = mapGames.get(id);

        return Optional.ofNullable(controller);
    }



}
