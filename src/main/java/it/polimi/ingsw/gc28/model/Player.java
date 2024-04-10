package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.cards.CardInitial;
import it.polimi.ingsw.gc28.model.cards.CardObjective;
import it.polimi.ingsw.gc28.model.cards.CardResource;
import it.polimi.ingsw.gc28.model.errors.types.PlayerActionError;
import it.polimi.ingsw.gc28.model.objectives.Objective;

import java.util.ArrayList;
import java.util.Optional;


public class Player {

    private int points, objectivePoints;
    private boolean winner = false;

    private final String name;
    private CardObjective objectiveChosen;

    private  ArrayList<CardObjective> objectivesToChoose;

    private ArrayList<CardResource> hand;
    private CardInitial cardInitial;

    private Table table;

    private PlayerActionError error;

    public Player(String name) {
        this.name = name;
        this.points = 0;
        this.objectivePoints = 0;
        this.hand = new ArrayList<>();
        this.table = new Table();

    }

    public ArrayList<CardObjective> getObjectivesToChoose(){
        return objectivesToChoose;
    }

    public void setObjectivesToChoose(ArrayList<CardObjective> objectivesToChoose) {
        this.objectivesToChoose = objectivesToChoose;
    }

    public void setObjectiveChosen(CardObjective card){
        objectiveChosen = card;
    }

    public Optional<CardObjective> getObjectiveChosen(){
        return Optional.ofNullable(objectiveChosen) ;
    }

    public void setError(PlayerActionError error) {
        this.error = error;
    }

    public Optional<PlayerActionError> getError(){
        return Optional.ofNullable(error);
    }

    public ArrayList<CardResource> gethand (){
        return hand;
    }

    public int getPoints(){
        return points;
    }

    public void setWinner(){winner = true;}

    public boolean isWinner() {return winner;}

    public int getObjectivePoints() {return objectivePoints;}

    public void removeCard (CardGame cardToBeRemoved) {
        hand.remove(cardToBeRemoved);
    }
    public void addCardToHand(CardResource drawnCard){
        hand.add(drawnCard);
    }
    public Table getTable () {return table;}

    public String getName() {
        return name;
    }




    /**
     * this method update the points when a card is played
     * @param coordinate the coordinate of the played card
     */
    public void updatePoints(Coordinate coordinate){
        points += table.getCell(coordinate).points(table, coordinate);
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
            //potremmo creare una IrregularCoordinateException, da aggiungere
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

            table.updateCoordinate(coordinates);

            removeCard(playedCard);
            
        }
    }

    /**
     * This method adds points of the objectives to the player's score at the end of the game
     * @param objectives globalObjectives (newly created list)
     * @requires objective.isPresent()
     */
    public void calculateObjectivePoints(ArrayList<Objective> objectives){
        Optional<CardObjective> objChosen = getObjectiveChosen();

        if(objChosen.isPresent()){
            // I can add to this array since we pass a newly created ArrayList.
            objectives.add(objChosen.get().getObjective());
        }else{
            System.err.println("Objective not set");
        }

        for(Objective obj: objectives){
            objectivePoints += obj.calculatePoints(table.GetMapPositions(),table.getResourceCounters());
        }
    }
    public Optional<CardInitial> getCardInitial(){
        return Optional.ofNullable(cardInitial);
    }

    public void setCardInitial(CardInitial card){
        this.cardInitial = card;
    }



}
