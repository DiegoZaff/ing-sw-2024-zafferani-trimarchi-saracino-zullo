package it.polimi.ingsw.gc28.View.utils;

import it.polimi.ingsw.gc28.model.Vertex;
import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.resources.ResourcePrimary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class TuiStringHelper {

    public static ArrayList<String> getVerticesStringInfo(CardGame card, boolean isFront){

        Vertex[] vertices;

        if(isFront){
            vertices = card.getVerticesFront();
        }else{
            vertices = card.getVerticesBack();
        }

        return Arrays.stream(vertices).map(vertex -> {
            if (vertex.isExists()){
                if(vertex.getResource().isPresent()){
                    return vertex.getResource().get().toString();
                }else{
                    return "OO";
                }
            }else{
                return "  ";
            }
        }).collect(Collectors.toCollection(ArrayList::new));
    }
}
