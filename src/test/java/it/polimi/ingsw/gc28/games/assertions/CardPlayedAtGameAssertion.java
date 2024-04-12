package it.polimi.ingsw.gc28.games.assertions;

import it.polimi.ingsw.gc28.model.*;

import java.util.Optional;

public class CardPlayedAtGameAssertion extends  GameAssertion{

    private final String nickname;
    private final String card;

    private final int x;

    private final int y;

    private final boolean isFront;

    private Boolean isFrontActual;

    private String cardIdActual;

    public CardPlayedAtGameAssertion(String card, String nickname, int x, int y, boolean isFront){
        this.nickname = nickname;
        this.card = card;
        this.x = x;
        this.y = y;
        this.isFront = isFront;
        this.cardIdActual = null;
        this.isFrontActual = null;
    }

    @Override
    public boolean verifyAssertion(Game game) {
        Optional<Player> player = super.getPlayerFromNick(nickname, game);

        if(player.isEmpty()){
            System.err.println("Non existent player");
            cardIdActual = null;
            isFrontActual = null;
            return false;
        }

        Table table = player.get().getTable();

        Cell cell = table.getCell(new Coordinate(this.x, this.y));

        if(cell == null){
            cardIdActual = null;
            isFrontActual = null;
            return false;
        }

        isFrontActual = cell.getIsPlayedFront();

        cardIdActual = cell.card.getId();

        return cardIdActual.equals(card) && isFrontActual == this.isFront;
    }

    @Override
    public String toString() {
        return String.format("CardPlayedAtGameAssertion --- expected cell: %s, %s, actual: %s, %s", card, isFront, cardIdActual, isFrontActual);
    }
}
