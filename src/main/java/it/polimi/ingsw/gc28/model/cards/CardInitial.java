package it.polimi.ingsw.gc28.model.cards;

import it.polimi.ingsw.gc28.model.Cell;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Table;
import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.model.resources.Resource;
import it.polimi.ingsw.gc28.model.Vertex;
import it.polimi.ingsw.gc28.model.resources.ResourcePrimary;
import it.polimi.ingsw.gc28.model.resources.utils.ResourcePrimaryType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceType;
import it.polimi.ingsw.gc28.view.utils.Colors;
import it.polimi.ingsw.gc28.view.utils.TuiStringHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CardInitial extends CardGame {

    private final Vertex[] verticesBack;
    private final Map<Resource, Integer> centralResources;
    public CardInitial(String id, ResourceType[] resourcesBack, ResourceType[] resourcesFront, ResourceType[] primaryResources){
        super(id, resourcesFront);

        Vertex[] vertexBack = new Vertex[4];
        Resource[] resBack = new Resource[4];

        for (int i=0; i<resourcesBack.length; i++) {
            if(resourcesBack[i] == ResourceType.FOX) {
                resBack[i] = new ResourcePrimary(ResourcePrimaryType.FOX);
            } else if (resourcesBack[i] == ResourceType.LEAF) {
                resBack[i] = new ResourcePrimary(ResourcePrimaryType.LEAF);
            } else if (resourcesBack[i] == ResourceType.BUTTERFLY) {
                resBack[i] = new ResourcePrimary(ResourcePrimaryType.BUTTERFLY);
            } else if (resourcesBack[i] == ResourceType.MUSHROOM) {
                resBack[i] = new ResourcePrimary(ResourcePrimaryType.MUSHROOM);
            } else {
                resBack[i] = null;
            }
        }

        for (int i=0; i< resBack.length; i++) {
            if(resBack[i]!=null) {
                vertexBack[i] = new Vertex(resBack[i]);
            }
        }
        this.verticesBack=vertexBack;

        Resource[] resCenter = new Resource[3];
        for (int i=0; i<primaryResources.length; i++) {
            if (primaryResources[i] == ResourceType.FOX) {
                resCenter[i] = new ResourcePrimary(ResourcePrimaryType.FOX);
            } else if (primaryResources[i] == ResourceType.LEAF) {
                resCenter[i] = new ResourcePrimary(ResourcePrimaryType.LEAF);
            } else if (primaryResources[i] == ResourceType.BUTTERFLY) {
                resCenter[i] = new ResourcePrimary(ResourcePrimaryType.BUTTERFLY);
            } else if (primaryResources[i] == ResourceType.MUSHROOM) {
                resCenter[i] = new ResourcePrimary(ResourcePrimaryType.MUSHROOM);
            } else {
                resCenter[i] = null;
            }
        }
        centralResources = new HashMap<>();
        createCentralResources(centralResources, resCenter);
    }

    public void createCentralResources(Map<Resource, Integer> centralResources, Resource[] Resources){
        for(Resource resource: Resources){
            if (resource != null) {
                centralResources.put(resource, 1);
            }
        }
    }


    @Override
    public Optional<ResourcePrimary> getObjectiveResource() {
        return Optional.empty();
    }

    @Override
    public int playFront(Table table, Coordinate playCoordinate){
        Cell cell = new Cell(this, 0, true);
        table.addMapPosition(playCoordinate, cell);
        table.removePlayableCoordinate(playCoordinate);
        return 0;
    }
    @Override
    public void playBack(Table table, Coordinate playCoordinate){
        Cell cell = new Cell(this, 0, false);
        table.addMapPosition(playCoordinate, cell);
        table.removePlayableCoordinate(playCoordinate);
    }

    /**
     * this method provides a map that contains the number of resource in the vertex in the back of the card
     * @return  Map<Resource, Integer> Integer: the number of a resource in a card
     */
    @Override
    public Map<Resource,Integer> getFrontCardResource (){
        Map<Resource,Integer> mapResource = super.getFrontCardResource();
        int temp;
        for (Resource r : centralResources.keySet()){
            if (!mapResource.containsKey(r)){
                mapResource.put(r, 1);
            }else {
                temp = mapResource.get(r) +1;
                mapResource.replace(r, temp);
            }
        }
        return mapResource;
    }

    @Override
    public int getPoints(Table table, Coordinate coordinate){
        return 0;
    }

    @Override
    public Map<Resource,Integer> getBackCardResource(){
        return getResourceIntegerMap(verticesBack);
    }

    @Override
    public Vertex[] getVerticesBack() {
        return verticesBack;
    }

    @Override
    public ActionType getIntendedAction(){
        return ActionType.PLAY_INITIAL_CARD;
    }

    @Override
    public String getCentralResourceStringInfo(boolean isFront) {
        if(!isFront) return Colors.RESET.getCode()+"  "+Colors.RESET.getCode();

        StringBuffer info = new StringBuffer();

        for(Resource res : centralResources.keySet()){
            for (int i = 0; i<centralResources.get(res); i++){
                info.append(res.toString());
            }
        }

        return info.toString();
    }
    private int getCentralResourceNumber(boolean isFront){
        if(!isFront) return 1;
        int x = 0;
        for (Resource res : centralResources.keySet()){
            x += centralResources.get(res);
        }
        return x;
    }


    public String toString(boolean isFront) {
        /*
        StringBuffer show = new StringBuffer(super.toString(isFront));
        String x = this.getCentralResourceStringInfo(isFront);
        int offset = x.length()/2;
        show.replace(47-offset,47+offset, this.getCentralResourceStringInfo(isFront));
        return show.toString();

         */
        ArrayList<String> verticesStrings = TuiStringHelper.getVerticesStringInfo(this, isFront);


        String show = String.format("""
                        __________________
                        |%s            %s|
                        |       %s       |
                        |%s            %s|
                        ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
                        """, verticesStrings.get(0), verticesStrings.get(1), this.getCentralResourceStringInfo(isFront),
                verticesStrings.get(3), verticesStrings.get(2));


        StringBuffer s = new StringBuffer(show);
        int offset = this.getCentralResourceNumber(isFront)-1;


        /*
        for(int i = 0; i<s.length(); i++){
            System.out.println("char at "+i+": "+ s.charAt(i));
        }
        */

        s.delete(58,58+offset);

        int x = 62+11*getCentralResourceNumber(isFront)+1;
        s.delete(x,x+offset);

        return s.toString();


    }

}


