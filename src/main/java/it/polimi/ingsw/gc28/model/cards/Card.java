package it.polimi.ingsw.gc28.model.cards;

/**
 * The purpose of this empty abstract class is to have a hierarchy that
 * allows us to perform shuffling and drawing of cards inside `Deck` in
 * a general way.
 */
public abstract class Card {
    final private String id;

    protected Card(String id) {
        this.id = id;
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
