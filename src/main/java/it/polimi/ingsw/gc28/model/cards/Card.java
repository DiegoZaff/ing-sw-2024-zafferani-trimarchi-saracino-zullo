package it.polimi.ingsw.gc28.model.cards;

import javafx.scene.image.ImageView;

import java.io.Serializable;

/**
 * The purpose of this empty abstract class is to have a hierarchy that
 * allows us to perform shuffling and drawing of cards inside `Deck` in
 * a general way.
 */
public abstract class Card implements Serializable {
    final private String id;
    //private final ImageView frontImg;
    //private final ImageView backImg;


    protected Card(String id) {
        this.id = id;
        //this.frontImg = front;
        //this.backImg = backImg;
    }

    public String getId(){
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Card other = (Card) obj;
        return this.id.equals(other.id);
    }

    @Override
    public String toString(){
        return id;
    }
}
