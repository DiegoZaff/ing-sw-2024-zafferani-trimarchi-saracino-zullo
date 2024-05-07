package it.polimi.ingsw.gc28.controller;
import it.polimi.ingsw.gc28.View.GameRepresentation;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.cards.CardObjective;
import it.polimi.ingsw.gc28.model.cards.CardResource;
import it.polimi.ingsw.gc28.model.cards.CardsManager;
import it.polimi.ingsw.gc28.model.chat.ChatMessage;
import it.polimi.ingsw.gc28.model.errors.types.NoSuchCardId;
import it.polimi.ingsw.gc28.model.errors.PlayerActionError;
import it.polimi.ingsw.gc28.network.messages.server.*;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GameController {
    final Game gameModel;

    private final Map<String, VirtualView> clients;

    public GameController(Game gameModel) {
        this.gameModel = gameModel;
        this.clients = new HashMap<>();
    }

    public void addPlayerToGame(String name, VirtualView client, boolean notifyJoin) throws RemoteException {

        synchronized (gameModel){
            try{
                this.gameModel.addPlayerToGame(name);
                this.clients.put(name, client);
            }catch (PlayerActionError e){
                // notify error to player
                MsgReportError message = new MsgReportError(name, e.getMessage());
                client.sendMessage(message);
                return;
            }


            if(notifyJoin){
                int playersLeftToJoin = gameModel.getNPlayers() - gameModel.getActualNumPlayers();

                MsgOnGameJoined message = new MsgOnGameJoined(this.gameModel.getGameId() ,name, playersLeftToJoin);

                clients.get(name).sendMessage(message);
            }

            if(hasGameStarted()){
                for(Map.Entry<String, VirtualView> entry : clients.entrySet()){

                    VirtualView cli = entry.getValue();

                    GameRepresentation representation = getGameRepresentation();

                    MsgOnGameStarted m = new MsgOnGameStarted(representation);

                    cli.sendMessage(m);
                }
            }
        }
    }

    public boolean hasGameStarted(){
        synchronized (gameModel){
            return gameModel.getHasGameStarted();
        }
    }


    /**
     * Overloading, in case we have already calculated Player.
     */
    public Optional<ArrayList<CardObjective>> getPersonalObjectives(Player player){
        synchronized (gameModel) {
            return player.getObjectivesToChoose();
        }
    }

    public ArrayList<CardResource> getPlayerHand(Player player){
        synchronized (gameModel) {
            return player.gethand();
        }
    }


    public void chooseObjectivePersonal(String name, String cardId) throws RemoteException {

        Optional<CardObjective> chosen = CardsManager.getInstance().getCardObjectiveFromId(cardId);

        if(chosen.isEmpty()){
            notifyError(name, new NoSuchCardId(cardId), "ChooseObjective");
            return;
        }

        synchronized (gameModel){

            try{
                gameModel.chooseObjective(name, chosen.get());

                notifyObjChosen(name);
            }catch (PlayerActionError e){
                notifyError(name, e, "ChooseObjective");
            }
        }
    }

    public void drawCard(String name, boolean fromGoldDeck) throws RemoteException {
        synchronized (gameModel){
            try{
                CardResource card = gameModel.drawGameCard(name, fromGoldDeck);

                notifyOfCardDrawn(name, card, fromGoldDeck);
            }catch (PlayerActionError e){
                notifyError(name, e, "DrawCardFromDeck");
            }
        }
    }

    public void drawCard(String playerName, String cardId) throws RemoteException {
        Optional<? extends CardResource> cardToDraw = CardsManager.getInstance().getCardResourceFromId(cardId);

        if(cardToDraw.isEmpty()){
            notifyError(playerName, new NoSuchCardId(cardId), "DrawCardFromCardId");
            return;
        }

        synchronized (gameModel){
            try {
                gameModel.drawGameCard(playerName, cardToDraw.get());

                notifyOfCardDrawn(playerName, cardToDraw.get());
            } catch (PlayerActionError e) {
                notifyError(playerName, e, "DrawCardFromCardId");
            }
        }
    }


    public void playCard(String playerName, String cardId, boolean isFront, Coordinate coordinate) throws RemoteException {
        Optional<? extends CardGame> cardToPlay = CardsManager.getInstance().getCardGameFromId(cardId);

        if(cardToPlay.isEmpty()){
            notifyError(playerName, new NoSuchCardId(cardId), "PlayCard");
            return;
        }

        synchronized (gameModel){
            try{
                gameModel.playGameCard(playerName, cardToPlay.get(), isFront, coordinate);

                Optional<Player> playerWhoPlayed = gameModel.getPlayerOfName(playerName);

                if(playerWhoPlayed.isEmpty()){
                    // TODO: handle this... should not happen
                    throw new RuntimeException("Something went seriously wrong!");
                }

                notifyOfCardPlayed(playerName, cardToPlay.get().getId());

            }catch (PlayerActionError e){
                MsgReportError message = new MsgReportError(playerName, e.getMessage());
                try {
                    clients.get(playerName).sendMessage(message);
                    return;
                } catch (RemoteException ex) {
                    System.err.println("Could not notify client! Maybe disconnected?");
                    System.err.println(e.getMessage());
                    throw new RuntimeException(ex);
                }
            }

        }
    }

    public void sendMessage(ChatMessage chatMessage){
        gameModel.sendMessage(chatMessage);
        //TODO : vanno aggiunti i controlli

        //TODO: vanno aggiunte le notify
    }

    public void notifyOfCardDrawn(String playerName, CardResource card, Boolean fromGoldDeck) throws RemoteException {

        GameRepresentation gameRepresentation = getGameRepresentation();

        for(Map.Entry<String, VirtualView> entry : clients.entrySet()){

            VirtualView client = entry.getValue();

            MsgOnPlayerDrawnCard message = new MsgOnPlayerDrawnCard(gameRepresentation,playerName, card.getId(), fromGoldDeck);

            client.sendMessage(message);
        }
    }

    public void notifyOfCardDrawn(String playerName, CardResource card) throws RemoteException {

        GameRepresentation gameRepresentation = getGameRepresentation();

        for(Map.Entry<String, VirtualView> entry : clients.entrySet()){

            VirtualView client = entry.getValue();

            MsgOnPlayerDrawnCard message = new MsgOnPlayerDrawnCard(gameRepresentation,playerName, card.getId(), null);

            client.sendMessage(message);

        }
    }

    public void notifyOfCardPlayed(String playerWhoPlayed, String cardPlayedId) throws RemoteException {
        GameRepresentation gameRepresentation = getGameRepresentation();

        for(Map.Entry<String, VirtualView> entry : clients.entrySet()){

            VirtualView client = entry.getValue();


            MsgOnPlayerPlayedCard message = new MsgOnPlayerPlayedCard(gameRepresentation, cardPlayedId, playerWhoPlayed);
            client.sendMessage(message);
        }
    }

    public void notifyObjChosen(String playerName) throws RemoteException {

        GameRepresentation gameRepresentation = getGameRepresentation();

        for(Map.Entry<String, VirtualView> entry : clients.entrySet()){

            VirtualView client = entry.getValue();

            MsgOnPlayerChooseObjective message = new MsgOnPlayerChooseObjective(playerName, gameRepresentation);

            client.sendMessage(message);
        }
    }

    public void notifyError(String name, PlayerActionError e, String actionDetails) throws RemoteException {
        VirtualView clientOfRequest = clients.get(name);

        if(clientOfRequest == null){
            System.err.println(actionDetails + " from non existent player!");
            return;
        }

        MsgReportError message = new MsgReportError(name, actionDetails);
        clients.get(name).sendMessage(message);
    }

    public void notifyGameCreated(String gameId, String name, int numberOfPlayersLeftToJoin) throws RemoteException {
        VirtualView clientOfRequest = clients.get(name);

        if(clientOfRequest == null){
            System.err.println(" from non existent player!");
            return;
        }

        MsgOnGameCreated message = new MsgOnGameCreated(gameId, name, numberOfPlayersLeftToJoin);

        clients.get(name).sendMessage(message);

    }

    public void notifyChatMessage(){

    }

    public GameRepresentation getGameRepresentation(){
        synchronized (gameModel){
            return gameModel.getGameRepresentation();
        }
    }
}
