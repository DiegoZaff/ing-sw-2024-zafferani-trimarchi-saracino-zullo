package it.polimi.ingsw.gc28.games.assertions;

import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.model.Player;

import java.util.Optional;

public class CardPlayedAtGameAssertion extends  GameAssertion{

    private final String nickname;
    private final String card;

    public CardPlayedAtGameAssertion(String card, String nickname){
        this.nickname = nickname;
        this.card = card;
    }

    @Override
    public boolean verifyAssertion(Game game) {
        Optional<Player> player = game.getPlayers().stream().filter((p) -> p.getName().equals(nickname)).findFirst();

        if(player.isEmpty()){
            System.err.println("Non existent player");
            return false;
        }

        return false;
    }

    @Override
    public String toString() {
        return null;
    }
}
