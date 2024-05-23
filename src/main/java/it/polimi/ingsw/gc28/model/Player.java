package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.errors.types.NotOwnedCard;
import it.polimi.ingsw.gc28.model.utils.PlayerColor;
import it.polimi.ingsw.gc28.view.PrivateRepresentation;
import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.cards.CardInitial;
import it.polimi.ingsw.gc28.model.cards.CardObjective;
import it.polimi.ingsw.gc28.model.cards.CardResource;
import it.polimi.ingsw.gc28.model.errors.PlayerActionError;
import it.polimi.ingsw.gc28.model.errors.types.NotPlayableGoldCard;
import it.polimi.ingsw.gc28.model.errors.types.UnplayableCoordinate;
import it.polimi.ingsw.gc28.model.objectives.Objective;

import java.util.ArrayList;
import java.util.Optional;


public class Player {

    private int points, objectivePoints;

/*
    //setter per test calculatewinner è una schifezza si puo togliere;
    public void setPoints (int pointstoset) {points = pointstoset;};
    public void setObjectrivePoints (int pointstoset) {objectivePoints = pointstoset;};
*/

    private boolean winner = false;

    private final String name;
    private PlayerColor color;
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

    public Optional<ArrayList<CardObjective>> getObjectivesToChoose(){
        return Optional.ofNullable(objectivesToChoose);
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
        return points + objectivePoints;
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
     * if the card is played increments points and modify table resources counters.
     * if the card is played it removes it from hand
     * it throws an IrregularCardException if the card can not be played.
     * it throws an IrregularCoordinateException if the coordinated can not be played.
     * @param playedCard the card to be played
     * @param isFront indicate how the card has to be played, front if True, back if False
     * @param coordinates indicates the coordinate where the card has to be played
     */
    public void playCard(CardGame playedCard, boolean isFront, Coordinate coordinates) throws PlayerActionError {
        if (playedCard.checkHand(hand, cardInitial)){
            throw new NotOwnedCard(playedCard);
        }

        if (!table.checkPlayability(coordinates)){
            // TODO : maybe make more granular, why cant be played...
            throw new UnplayableCoordinate(coordinates);
        }else {
            int flag = 0;
            if (isFront){
                flag = playedCard.playFront(table, coordinates);
            } else {
                playedCard.playBack(table, coordinates);
            }
            if (flag != 0){
                throw new NotPlayableGoldCard(playedCard.getId());
            }

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
            objectivePoints += obj.calculatePoints(table.getMapPositions(),table.getResourceCounters());
        }
    }
    public Optional<CardInitial> getCardInitial(){
        return Optional.ofNullable(cardInitial);
    }

    public void setCardInitial(CardInitial card){
        this.cardInitial = card;
    }




    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Player other = (Player) obj;
        return name.equals(other.name);
    }

    public PrivateRepresentation getState(){
        return new PrivateRepresentation(objectiveChosen, table, hand, cardInitial, objectivesToChoose, color);
    }

    public void setColor(PlayerColor color){
        this.color = color;
    }

    public PlayerColor getColor(){
        return color;
    }
}
