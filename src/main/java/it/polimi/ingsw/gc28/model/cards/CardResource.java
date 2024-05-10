package it.polimi.ingsw.gc28.model.cards;

import it.polimi.ingsw.gc28.model.*;
import it.polimi.ingsw.gc28.model.errors.types.NotDrawableCardError;
import it.polimi.ingsw.gc28.model.errors.PlayerActionError;
import it.polimi.ingsw.gc28.model.resources.Resource;
import it.polimi.ingsw.gc28.model.resources.ResourcePrimary;
import it.polimi.ingsw.gc28.model.resources.utils.ResourcePrimaryType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceType;
import it.polimi.ingsw.gc28.view.utils.TuiStringHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class CardResource extends CardGame {
    private final ResourcePrimary resource;
    private final int pointsPerPlay;

    public CardResource(String id, ResourceType[] resourceCard, ResourcePrimaryType resource, int pointsPerPlay){
        super(id, resourceCard);
        this.pointsPerPlay = pointsPerPlay;
        this.resource = new ResourcePrimary(resource);
    }

    public CardResource(String id,ResourcePrimary resource, int pointsPerPlay, Vertex[] verticesFront){
        super(id, verticesFront);
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

        Optional<ResourcePrimary> centralRes1 = this.getObjectiveResource();
        String centralRes1String = "";
        if(centralRes1.isPresent()){
            centralRes1String = centralRes1.get().toString();
        }else{
            centralRes1String = "  ";
        }

        return centralRes1String;    }


    public void drawFaceUpCard(ArrayList<CardResource> faceUpResCards, ArrayList<CardGold> faceUpGoldCards, Deck deck, Player player) throws PlayerActionError {
        if(!faceUpResCards.contains(this)){
            throw new NotDrawableCardError(this.getId());
        }
        faceUpResCards.remove(this);

        Optional<CardResource> cardToAddToVisible = deck.nextResource();

        cardToAddToVisible.ifPresent(faceUpResCards::add);

        player.addCardToHand(this);

    }

    @Override
    public String toString(boolean isFront){
        /*
        ArrayList<String> verticesStrings = TuiStringHelper.getVerticesStringInfo(this, isFront);
        String centralRes = this.getCentralResourceStringInfo(isFront);

        String show = String.format("""
                    __________________
                    |%s            %s|
                    |       %s       |
                    |%s            %s|
                    ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
                """, verticesStrings.get(0), verticesStrings.get(1),centralRes,
                verticesStrings.get(3), verticesStrings.get(2));
        return show;
         */
        StringBuffer show = new StringBuffer(super.toString(isFront));
        show.replace(46,48, this.getCentralResourceStringInfo(isFront));
        return show.toString();

    }
}

