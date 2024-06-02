package it.polimi.ingsw.gc28.model.cards;

import it.polimi.ingsw.gc28.model.*;
import it.polimi.ingsw.gc28.model.errors.types.NotDrawableCardError;
import it.polimi.ingsw.gc28.model.errors.PlayerActionError;
import it.polimi.ingsw.gc28.model.resources.Resource;
import it.polimi.ingsw.gc28.model.resources.ResourcePrimary;
import it.polimi.ingsw.gc28.model.resources.utils.ResourcePrimaryType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceType;
import it.polimi.ingsw.gc28.view.utils.Colors;
import it.polimi.ingsw.gc28.view.utils.TuiStringHelper;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class CardResource extends CardGame {
    private final ResourcePrimary resource;
    private final int pointsPerPlay;

    public CardResource(String id, ResourceType[] resourceCard, ResourcePrimaryType resource, int pointsPerPlay, String frontImage, String backImage){
        super(id, resourceCard, frontImage, backImage);
        this.pointsPerPlay = pointsPerPlay;
        this.resource = new ResourcePrimary(resource);
    }

    public CardResource(String id,ResourcePrimary resource, int pointsPerPlay, Vertex[] verticesFront, String frontImage, String backImage){
        super(id, verticesFront, frontImage, backImage);
        this.resource = resource;
        this.pointsPerPlay = pointsPerPlay;
    }

    @Override
    public Optional<ResourcePrimary> getObjectiveResource() {
        return Optional.of(resource);
    }

    @Override
    public int playFront(Table table, Coordinate playCoordinate){

        Cell cell = new Cell(this, table.getMapPositions().size(), true);
        table.addMapPosition(playCoordinate, cell);
        table.removePlayableCoordinate(playCoordinate);
        return 0;
    }


    @Override
    public void playBack(Table table, Coordinate playCoordinate){
        Cell cell = new Cell(this, table.getMapPositions().size(), false);
        table.addMapPosition(playCoordinate, cell);
        table.removePlayableCoordinate(playCoordinate);
    }

    @Override
    public int getPoints(Table table, Coordinate coordinate){
        return pointsPerPlay;
    }

    @Override
    public Map<Resource,Integer> getBackCardResource(){
        Map<Resource, Integer> m = new HashMap<>();
        m.put(resource, 1);
        return m;
    }

    @Override
    public Vertex[] getVerticesBack(){
        Vertex[] verticesBack = new Vertex[4];
        Vertex v = new Vertex(true);
        for (int i = 0; i<= 3; i++){
            verticesBack[i] = v;
        }
        return verticesBack;
    }

    @Override
    public String getCentralResourceStringInfo(boolean isFront) {
        if(!isFront){
            return  resource.toString();
        }

        return Colors.RED.getCode()+"  "+Colors.RESET.getCode();

    }


    public void drawFaceUpCard(ArrayList<CardResource> faceUpResCards, ArrayList<CardGold> faceUpGoldCards, Deck deck, Player player) throws PlayerActionError {
        if(!faceUpResCards.contains(this)){
            throw new NotDrawableCardError(this.getId());
        }
        faceUpResCards.remove(this);

        Optional<CardResource> cardToAddToVisible = deck.nextResource();

        cardToAddToVisible.ifPresent(faceUpResCards::add);

        player.addCardToHand(this);

    }


    public String toString(boolean isFront){

        ArrayList<String> verticesStrings = TuiStringHelper.getVerticesStringInfo(this, isFront);
        String centralRes = this.getCentralResourceStringInfo(isFront);
        String color = resource.getType().getResourceColor();

        String show = String.format("""
                %s__________________
                %s|%s     %s      %s|
                %s|       %s       |
                %s|%s            %s|
                %s‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾%s
                """,color, color, verticesStrings.get(0),""+pointsPerPlay ,verticesStrings.get(1)+color,color,centralRes+color,color,
                verticesStrings.get(3), verticesStrings.get(2)+color,color,Colors.RESET.getCode());

        if (!isFront){
            StringBuffer s = new StringBuffer(show);
            s.replace(46, 47, " ");
            return s.toString();
        }

        return show;



    }

    @Override
    public boolean checkHand(ArrayList<CardResource> hand, CardInitial cardInitial){
        return !(hand.contains(this));
    }

}

