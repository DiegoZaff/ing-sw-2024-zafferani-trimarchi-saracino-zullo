package it.polimi.ingsw.gc28.controller;
import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.model.cards.CardObjective;
import it.polimi.ingsw.gc28.model.cards.CardResource;

import java.util.ArrayList;
import java.util.Optional;

public class GameController {
    final Game gameModel;

    // TODO : attributo per memorizza i client connessi alla partita (sia RMI che socket)
    public GameController(Game gameModel) {
        this.gameModel = gameModel;
    }

    public void addPlayerToGame(String name) throws RuntimeException{
        synchronized (gameModel){
            this.gameModel.addPlayerToGame(name);
        }
    }

    public boolean hasGameStarted(){
        synchronized (gameModel){
            return gameModel.getHasGameStarted();
        }
    }

    public Optional<ArrayList<CardObjective>> getPersonalObjectives(String name){
        synchronized (gameModel) {
            Optional<Player> p = getPlayerOfName(name);

            if (p.isEmpty()) {
                return Optional.of(new ArrayList<>());
            }

            return p.get().getObjectivesToChoose();
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

    public Optional<Player> getPlayerOfName(String name){
        synchronized (gameModel) {
            return gameModel.getPlayers().stream().filter((player -> player.getName().equals(name))).findFirst();
        }
    }

    public void chooseObjectivePersonal(String name, int n) throws IllegalArgumentException, IllegalStateException{
        synchronized (gameModel){
            Optional<Player> player = getPlayerOfName(name);

            if(player.isEmpty()){
                throw new IllegalArgumentException("This player doesn't belong to this game");
            }

            if(player.get().getObjectiveChosen().isPresent()){
                throw new IllegalArgumentException("You have already chosen the objective!");
            }

            if(n < 0 || n > 1){
                throw new IllegalArgumentException("You must choose either the first or the second card.");
            }

            Optional<ArrayList<CardObjective>> objs = getPersonalObjectives(player.get());

            if(objs.isEmpty() || objs.get().size() != 2){
                throw new IllegalStateException("Bad State in personal objectives");
            }

            CardObjective chosen = objs.get().get(n);

            gameModel.chooseObjective(player.get(), chosen);
        }
    }

    public void drawCardFomDeck(String name, boolean fromGoldDeck) throws IllegalArgumentException{
        synchronized (gameModel){
            Optional<Player> player = getPlayerOfName(name);

            if(player.isEmpty()){
                throw new IllegalArgumentException("This player doesn't belong to this game");
            }

            ActionType expectedAction = getExpectedAction();

            //probabilmente questa verifica va fatta nel controller della view//
            if(expectedAction != ActionType.DRAW_CARD){
                throw new IllegalStateException("You can't draw a card in this moment");
            }

            ArrayList<CardResource> hand = getPlayerHand(player.get());

            if(hand.size() != 2){
                throw new IllegalStateException("Bad state in player's hand");
            }

            gameModel.drawGameCard(player.get(), fromGoldDeck);
        }
    }

    public ActionType getExpectedAction(){
        return gameModel.actionExpected();
    }

    public Optional<Player> getPlayerOfTurn(){
        return gameModel.playerToPlay();
    }

    //in this class we need to implement the logic behind the call of the methods using syncronized
}
