package it.polimi.ingsw.gc28.model.cards;

import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Deck;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.Table;
import it.polimi.ingsw.gc28.model.challenge.Challenge;
import it.polimi.ingsw.gc28.model.challenge.PositionChallenge;
import it.polimi.ingsw.gc28.model.challenge.ResourceChallenge;
import it.polimi.ingsw.gc28.model.challenge.utils.ChallengeType;
import it.polimi.ingsw.gc28.model.errors.types.NotDrawableCardError;
import it.polimi.ingsw.gc28.model.errors.PlayerActionError;
import it.polimi.ingsw.gc28.model.resources.Resource;
import it.polimi.ingsw.gc28.model.resources.ResourcePrimary;
import it.polimi.ingsw.gc28.model.resources.ResourceSpecial;
import it.polimi.ingsw.gc28.model.resources.utils.ResourcePrimaryType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceSpecialType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceType;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class CardGold extends CardResource {
    private Map<ResourcePrimary, Integer> playability;
    private Challenge challenge;

    public Optional<Challenge> getChallenge(){
        return Optional.ofNullable(challenge);
    }


    public CardGold(String id, ResourceType[] resourceCard, ResourcePrimaryType resourcePrimary, int pointsPerPlay,
                    ResourcePrimaryType[] resourceNeeded, ChallengeType challenge, ResourceSpecialType resourceChallenge,
                    String frontImg, String backImg){
        super(id, resourceCard, resourcePrimary, pointsPerPlay, frontImg, backImg);
        createPlayabilityMap(resourceNeeded);
        if(challenge != null){
            if(challenge.equals(ChallengeType.POINTS_PER_COVER)){
                this.challenge = new PositionChallenge(challenge);
            }else{
                this.challenge = new ResourceChallenge(challenge, new ResourceSpecial(resourceChallenge));
            }
        }
        //this.resourceChallenge =Optional.of(new ResourceSpecial(resourceChallenge));
        //this.challenge = new Challenge(challenge, resourceChallenge);
        }

    /**
     * this method create the hashMap to keep information about card's playability
     * @param resourceNeeded is the array related to the json file
     */
    public void createPlayabilityMap(ResourcePrimaryType[] resourceNeeded){
        playability = new HashMap<>();
        for(ResourcePrimaryType resourceType : resourceNeeded){
            if (resourceType != null) {
                ResourcePrimary res = new ResourcePrimary(resourceType);
                if (playability.containsKey(res)) {
                    playability.put(res, playability.get(res) + 1);
                } else playability.put(res, 1);
            }
        }
    }

    private  boolean checkCardPlayability(Table table){
        for (ResourcePrimary r : playability.keySet()){
            if (playability.get(r) > table.getResourceCounters().get(r)){
                return false;
            }
        }
        return true;
    }

    @Override
    public int playFront(Table table, Coordinate playCoordinate){
        if (checkCardPlayability(table)){
            return super.playFront(table, playCoordinate);
        } else {
            return -1;
        }
    }

    @Override
    public int getPoints(Table table, Coordinate coordinate){
        return getChallenge().map(value -> value.challengePoints(table, coordinate)).orElseGet(() -> super.getPoints(table, coordinate));
    }
   @Override
    public void drawFaceUpCard(ArrayList<CardResource> faceUpResCards, ArrayList<CardGold> faceUpGoldCards, Deck deck, Player player) throws PlayerActionError {
        if(!faceUpGoldCards.contains(this)){
            throw new NotDrawableCardError(this.getId());
        }
        faceUpGoldCards.remove(this);

        Optional<CardGold> cardGoldAddToVisible = deck.nextGold();

       cardGoldAddToVisible.ifPresent(faceUpGoldCards::add);

       player.addCardToHand(this);
    }


    private String resourceNeededToString (){
        int i;
        StringBuffer s = new StringBuffer();
        for (ResourcePrimary r : playability.keySet()){
            for (i = 0; i< playability.get(r); i++){
                s.append(r.toString());
            }
        }
        return s.toString();
    }

    @Override
    public String toString(boolean isFront){
        StringBuffer show = new StringBuffer(super.toString(isFront));
        /*
        for (int i = 0; i<show.length(); i++){
            System.out.println("char at "+i+": "+show.charAt(i));
        }

        punteggio a 46
         */
        if (getChallenge().isPresent() && isFront){
            show.replace(45,50, challenge.toString());
        }
        if (!isFront){
            show.replace(45, 49, "gold");
        }

        return show.toString();
    }

    @Override
    public String toString(){
        return super.toString()+", resource needed: "+resourceNeededToString();
    }


}


