package it.polimi.ingsw.gc28.model.objectives;
import it.polimi.ingsw.gc28.model.Cell;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.objectives.positions.PositionType;
import it.polimi.ingsw.gc28.model.resources.Resource;
import it.polimi.ingsw.gc28.model.resources.ResourcePrimary;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjectivePosition extends Objective {

    /**
     * this generalizes position patterns.
     * It describes the resource that each card must satisfy, from bottom left to top right.
     * for example, for a main diagonal it would be:
     *  [RED, RED, RED]
     */
    public final ResourcePrimary[] patternPosition;

    /**
     * this attribute stores the getNeighbors() function that generalizes different kinds of positions.
     */
    public final PositionType positionType;


    public ObjectivePosition(PositionType positionType, int points, ResourcePrimary[] patternPosition) {
        this.patternPosition = patternPosition;
        this.points = points;
        this.positionType = positionType;
    }

    @Override
    public int calculatePoints(Map<Coordinate, Cell> map,
                               Map<Resource, Integer> resourceCounters) throws  IllegalArgumentException{

        ArrayList<ArrayList<Coordinate>> combinationList = new ArrayList<>();

        for(Coordinate coordinate : map.keySet()){


            ArrayList<Coordinate> neighborsCoordinates = positionType.getNeighborsCoordinates(coordinate);


            ArrayList<CardGame> cardsNeighbors = neighborsCoordinates.stream()
                    .filter(map::containsKey)
                    .map(coord -> map.get(coord).card)
                    .collect(Collectors.toCollection(ArrayList::new));

            boolean isValid = validateNeighbors(cardsNeighbors, patternPosition);

            if(isValid){
                combinationList.add(neighborsCoordinates);
            }
        }

        ArrayList<ArrayList<Coordinate>> bestCombinations = new ArrayList<>();
        for (int i = 0; i < combinationList.size(); i++){
            ArrayList<ArrayList<Coordinate>> tempCombinations = new ArrayList<>();
            tempCombinations.add(combinationList.get(i));
            for(int j = 0; j < combinationList.size(); j++){
                if(i == j){
                    continue;
                }
                if(!areCombinationsOverlapping(combinationList.get(i), combinationList.get(j))){
                    tempCombinations.add(combinationList.get(j));
                }
            }

            if(tempCombinations.size() > bestCombinations.size()){
                // change pointer, tempCombinations will get reassigned.
                bestCombinations = tempCombinations;
            }
        }

        return bestCombinations.size() * points;
    }



    /**
     * validates card combination against objective pattern.
     *
     * @param cards cards of a combination
     * @param pattern resources that the cards must match
     * @return true if combination satisfies the pattern;
     * @example
     * pattern = [RED, RED, RED]
     * cards = [card1 with resourceType red,
     *          card2 with resourceType red,
     *          card3 with resourceType red]
     * return true.
     */
    public boolean validateNeighbors(ArrayList<CardGame> cards, ResourcePrimary[] pattern){
        if(cards.size() != pattern.length){
            return false;
        }
        for(int i = 0; i < cards.size(); i++){
            if(cards.get(i).getObjectiveResource().isEmpty() ||
                    (cards.get(i).getObjectiveResource().isPresent() &&
                            !pattern[i].type.equals(cards.get(i).getObjectiveResource().get().type ))){
                return false;
            }
        }
        return true;
    };
}
