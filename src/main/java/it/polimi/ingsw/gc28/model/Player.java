package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.errors.PlayerActionError;
import it.polimi.ingsw.gc28.model.objectives.Objective;

import java.util.ArrayList;
import java.util.Optional;
import java.util.OptionalInt;


public class Player {
    private int completedObjectives;

    private int points, objectivePoints;
    private int winner = 0;

    private final String name;
    private Objective objective;

    private ArrayList<CardGame> hand;

    private Table table;

    private Optional<PlayerActionError> error;

    public Player(String name) {
        this.name = name;
        this.points = 0;
        this.objectivePoints = 0;
        this.hand = new ArrayList<>();
        this.table = new Table();
        this.error = Optional.empty();
        this.completedObjectives = 0;
    }

    public void setError(Optional<PlayerActionError> error) {
        this.error = error;
    }

    public ArrayList<CardGame> gethand (){
        return hand;
    }

    public int getPoints(){
        return points;
    }

    public void setWinner(){winner = 1;}

    public int getWinner() {return winner;}

    public int getObjectivePoints() {return objectivePoints;}

    public void removeCard (CardGame cardToBeRemoved) {
        hand.remove(cardToBeRemoved);
    }

    public Table getTable () {return table;}


    /**
     * this method update the points when a card is played
     * @param coordinate the coordinate of the played card
     */
    public void updatePoints(Coordinate coordinate){
        points += table.getCell(coordinate).Points();
    }



    /**
     * this method play a card in the table of a player, checking if the coordinate and the card can be played.
     * if the card is played increments points and modify table recources counters.
     * if the card is played it removes it from hand
     * it throws an IrregularCardException if the card can not be played.
     * it throws an IrregularCoordinateException if the coordinated can not be played.
     * @param playedCard the card to be played
     * @param isFront indicate how the card has to be played, front if True, back if False
     * @param coordinates indicates the coordinate where the card has to be played
     */
    public void playCard(CardGame playedCard, boolean isFront, Coordinate coordinates){
        if (!table.checkPlayability(coordinates)){
            //potremmo creare una IrregularCordinateException, da aggiungere
            // ? maybe updates error attribute inside player and returns.
        }else {
            if (isFront){
                playedCard.playFront(table, coordinates);
            } else {
                playedCard.playBack(table, coordinates);
            }
            //se la carta non viene giocata (carta oro non giocabile) possiamo aggiungere una IrregularCardException
            //da controllare se viene usato il metodo corretto

            table.updateCounters(coordinates);

            this.updatePoints(coordinates);


            // manca update di playablePositions e unplayablePositions?


            //rimuove la carta dalla mano
            removeCard(playedCard);
            
        }
    }

    /**
     * This method adds points of the objectives to the player's score at the end of the game
     * @param objectives globalObjectives (newly created list)
     */
    public void calculateObjectivePoints(ArrayList<Objective> objectives){
        // I can add to this array since we pass a newly created ArrayList.
        objectives.add(objective);

        for(Objective obj: objectives){
            objectivePoints += obj.calculatePoints(table.GetMapPositions(),table.getResourceCounters());
        }
    }




}
