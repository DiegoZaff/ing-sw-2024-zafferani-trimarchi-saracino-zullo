package it.polimi.ingsw.gc28.controller;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.cards.CardObjective;
import it.polimi.ingsw.gc28.model.cards.CardResource;
import it.polimi.ingsw.gc28.model.errors.types.PlayerActionError;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GameController {
    final Game gameModel;

    private final Map<String, VirtualView> clients;

    // TODO : NEL MODEL!
    // TODO : attributo per memorizza i client connessi alla partita (sia RMI che socket)
    public GameController(Game gameModel) {
        this.gameModel = gameModel;
        this.clients = new HashMap<>();
    }

    public void addPlayerToGame(String name, VirtualView client){
        synchronized (gameModel){
            try{
                this.gameModel.addPlayerToGame(name);
                this.clients.put(name, client);
            }catch (PlayerActionError e){
                try {
                    client.reportError(e.getMessage());
                } catch (RemoteException ex) {
                    System.err.println(ex.getMessage());
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


    public void chooseObjectivePersonal(String name, String cardId) throws IllegalArgumentException, IllegalStateException{
        synchronized (gameModel){

            CardObjective chosen = CardsManager.getInstance().getObjectiveCard(cardId);

            try{
                gameModel.chooseObjective(name, chosen);

                notifyObjChosen(name, chosen);
            }catch (PlayerActionError e){
                notifyError(name, e, "ChooseObjective");
            }

            notifyOfNextTurn();
        }
    }

    public void drawCard(String name, boolean fromGoldDeck) throws IllegalArgumentException{
        synchronized (gameModel){
            try{
                CardResource card = gameModel.drawGameCard(name, fromGoldDeck);

                notifyOfCardDrawn(name, card, fromGoldDeck);
            }catch (PlayerActionError e){
                notifyError(name, e, "DrawCardFromDeck");
            }

            notifyOfNextTurn();
        }
    }

    public void drawCard(String playerName, String cardId) {
        CardResource cardToDraw = CardsManager.getInstance().getResourceCard(cardId);

        synchronized (gameModel){
            try {
                gameModel.drawGameCard(playerName, cardToDraw);

                notifyOfCardDrawn(playerName, cardToDraw);
            } catch (PlayerActionError e) {
                notifyError(playerName, e, "DrawCardFromCardId");
            }

            notifyOfNextTurn();
        }
    }


    public void playCard(String playerName, String cardId, boolean isFront, Coordinate coordinate){
        CardGame cardToPlay = CardsManager.getInstance().get(cardId);
        synchronized (gameModel){
            try{
                gameModel.playGameCard(playerName, cardToPlay, isFront, coordinate);

                Optional<Player> playerWhoPlayed = gameModel.getPlayerOfName(playerName);

                if(playerWhoPlayed.isEmpty()){
                    // TODO: handle this... should not happen
                    throw new RuntimeException("Something went seriously wrong!");
                }

                notifyOfCardPlayed(playerWhoPlayed.get(),cardToPlay, coordinate);
            }catch (PlayerActionError e){
                notifyError(playerName, e, "PlayCard");
            }

            notifyOfNextTurn();
        }
    }


    public void notifyOfNextTurn(){

        Optional<Player> playerOfTurn =  gameModel.playerToPlay();

        ActionType actionExpected = gameModel.actionExpected();

        String nextPlayer = null;

        if(playerOfTurn.isPresent()){
            nextPlayer = playerOfTurn.get().getName();
        }

        for(Map.Entry<String, VirtualView> entry : clients.entrySet()){

            VirtualView client = entry.getValue();

            try {
                String playerName = entry.getKey();

                client.onNextExpectedPlayerAction(actionExpected, nextPlayer);
            } catch (RemoteException e) {
                System.err.println("Could not notify client " + client + " about next turn");
            }
        }
    }


    public void notifyOfCardDrawn(String playerName, CardResource card, boolean fromGoldDeck){
        for(Map.Entry<String, VirtualView> entry : clients.entrySet()){

            VirtualView client = entry.getValue();

            try {
                client.onPlayerDrawnCard(playerName, card.getId(), fromGoldDeck);
            } catch (RemoteException e) {
                System.err.println("Could not notify client " + client + " about card " + card + " drawn from deck");
            }
        }
    }

    public void notifyOfCardDrawn(String playerName, CardResource card){
        for(Map.Entry<String, VirtualView> entry : clients.entrySet()){

            VirtualView client = entry.getValue();

            try {
                client.onPlayerDrawnCard(playerName, card.getId());
            } catch (RemoteException e) {
                System.err.println("Could not notify client " + client + " about card " + card + " drawn from visible cards");
            }
        }
    }

    public void notifyOfCardPlayed(Player playerWhoPlayed, CardGame card, Coordinate coord){
        for(Map.Entry<String, VirtualView> entry : clients.entrySet()){

            VirtualView client = entry.getValue();

            try {
                client.onPlayerPlayedCard(playerWhoPlayed.getName(), playerWhoPlayed.getTable(), playerWhoPlayed.getPoints());
            } catch (RemoteException e) {
                System.err.println("Could not notify client " + client + " about card " + card + " played at coordinate " + coord);
            }
        }
    }

    public void notifyObjChosen(String playerName, CardObjective cardObjective){
        for(Map.Entry<String, VirtualView> entry : clients.entrySet()){

            VirtualView client = entry.getValue();

            try {
                client.onPlayerChoseObjective(playerName, cardObjective.getId());
            } catch (RemoteException e) {
                System.err.println("Could not notify client " + client + " about objective chosen: " + cardObjective);
            }
        }
    }


    public void notifyError(String name, PlayerActionError e, String actionDetails){
        VirtualView clientOfRequest = clients.get(name);

        if(clientOfRequest == null){
            System.err.println(actionDetails + " from non existent player!");
            return;
        }

        try {
            clientOfRequest.reportError(e.getMessage());
        } catch (RemoteException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
