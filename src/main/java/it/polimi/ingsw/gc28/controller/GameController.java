package it.polimi.ingsw.gc28.controller;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.cards.CardObjective;
import it.polimi.ingsw.gc28.model.cards.CardResource;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;

import java.util.ArrayList;
import java.util.Optional;

public class GameController {
    final Game gameModel;

    // TODO : NEL MODEL!
    // TODO : attributo per memorizza i client connessi alla partita (sia RMI che socket)
    public GameController(Game gameModel) {
        this.gameModel = gameModel;
    }

    public void addPlayerToGame(String name, VirtualView client) throws RuntimeException{
        synchronized (gameModel){
            this.gameModel.addPlayerToGame(name, client);
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

            gameModel.chooseObjective(name, chosen);
        }
    }

    public void drawCard(String name, boolean fromGoldDeck) throws IllegalArgumentException{
        synchronized (gameModel){
            gameModel.drawGameCard(name, fromGoldDeck);
        }
    }

    public void drawCard(String playerName, String cardId) {
        CardResource cardToDraw = CardsManager.getInstance().getResourceCard(cardId);

        synchronized (gameModel){
            gameModel.drawGameCard(playerName, cardToDraw);
        }
    }


    public void playCard(String playerName, String cardId, boolean isFront, Coordinate coordinate){
        CardGame cardToPlay = CardsManager.getInstance().get(cardId);
        synchronized (gameModel){
            gameModel.playGameCard(playerName, cardToPlay, isFront, coordinate);
        }
    }

    public ActionType getExpectedAction(){
        return gameModel.actionExpected();
    }

    public Optional<Player> getPlayerOfTurn(){
        return gameModel.playerToPlay();
    }

    public void setNumberOfPlayers(int n){
        synchronized (gameModel) {
            gameModel.setNPlayers(n);
        }
    }
}
