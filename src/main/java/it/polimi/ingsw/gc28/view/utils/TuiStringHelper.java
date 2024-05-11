package it.polimi.ingsw.gc28.view.utils;

import it.polimi.ingsw.gc28.model.Vertex;
import it.polimi.ingsw.gc28.model.cards.CardGame;

import java.util.ArrayList;
import java.util.Arrays;
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
                    return Colors.WHITE.getCode()+"OO"+Colors.RESET.getCode();
                }
            }else{
                return Colors.WHITE.getCode()+"  "+Colors.RESET.getCode();
            }
        }).collect(Collectors.toCollection(ArrayList::new));
    }
}
