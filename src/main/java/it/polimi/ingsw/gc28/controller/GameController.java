package it.polimi.ingsw.gc28.controller;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.Table;
import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.cards.CardObjective;
import it.polimi.ingsw.gc28.model.cards.CardResource;
import it.polimi.ingsw.gc28.model.cards.CardsManager;
import it.polimi.ingsw.gc28.model.errors.types.NoSuchCardId;
import it.polimi.ingsw.gc28.model.errors.PlayerActionError;
import it.polimi.ingsw.gc28.network.messages.server.*;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;

import java.io.IOException;
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

    public void addPlayerToGame(String name, VirtualView client){
        synchronized (gameModel){
            try{
                this.gameModel.addPlayerToGame(name);
                this.clients.put(name, client);
            }catch (PlayerActionError e){
                MsgReportError message = new MsgReportError(name, e.getMessage());
            }

            MsgOnGameJoined message = new MsgOnGameJoined(name, gameModel.getNPlayers() - gameModel.getActualNumPlayers());
            clients.get(name).sendMessage(message);
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

        Optional<CardObjective> chosen = CardsManager.getInstance().getCardObjectiveFromId(cardId);

        if(chosen.isEmpty()){
            notifyError(name, new NoSuchCardId(cardId), "ChooseObjective");
            return;
        }

        synchronized (gameModel){

            try{
                gameModel.chooseObjective(name, chosen.get());

                notifyObjChosen(name, chosen.get());
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

            notifyOfNextTurn();
        }
    }


    public void playCard(String playerName, String cardId, boolean isFront, Coordinate coordinate){
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

                notifyOfCardPlayed(playerWhoPlayed.get(), cardToPlay.get(), coordinate);
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

            //try {
                String playerName = entry.getKey();

                //client.onNextExpectedPlayerAction(actionExpected, nextPlayer);//TODO : qui
           // } //catch (RemoteException e) {
               // System.err.println("Could not notify client " + client + " about next turn");
            //}
            // TODO : deve creare un messaggio di risposta
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
                client.onPlayerDrawnCard(playerName, card.getId());// qui va costruito e mandato il messaggio in tutte le notify ch corrispondono i messaggi. esattamme
                //TODO: sopraaaaaaaa

            } catch (RemoteException e) {
                System.err.println("Could not notify client " + client + " about card " + card + " drawn from visible cards");
            }
        }
    }

    public void notifyOfCardPlayed(String playerWhoPlayed, Table table, int newPlayerPoints){
        for(Map.Entry<String, VirtualView> entry : clients.entrySet()){

            VirtualView client = entry.getValue();

            MsgOnPlayerPlayedCard message = new MsgOnPlayerPlayedCard(playerWhoPlayed, table, newPlayerPoints);
            client.sendMessage(message);
        }
    }

    public void notifyObjChosen(String playerName, String cardId){
        for(Map.Entry<String, VirtualView> entry : clients.entrySet()){

            VirtualView client = entry.getValue();

            MsgOnPlayerChooseObjective message = new MsgOnPlayerChooseObjective(playerName, cardId);
            client.sendMessage(message);
        }
    }

    public void notifyError(String name, PlayerActionError e, String actionDetails) throws IOException {
        VirtualView clientOfRequest = clients.get(name);

        if(clientOfRequest == null){
            System.err.println(actionDetails + " from non existent player!");
            return;
        }

        MsgReportError message = new MsgReportError(name, actionDetails);
        clients.get(name).sendMessage(message);
    }

    public void notifyGameCreated(String gameId, String name, int numberOfPlayersLeftToJoin){
        VirtualView clientOfRequest = clients.get(name);

        if(clientOfRequest == null){
            System.err.println(" from non existent player!");
            return;
        }

        MsgOnGameCreated message = new MsgOnGameCreated(gameId, name, numberOfPlayersLeftToJoin);
        clients.get(name).sendMessage(message);
    }
}
