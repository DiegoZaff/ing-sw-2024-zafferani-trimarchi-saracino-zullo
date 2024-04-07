package it.polimi.ingsw.gc28.games.moves;

import it.polimi.ingsw.gc28.games.assertions.GameAssertion;
import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.model.Player;
import it.polimi.ingsw.gc28.model.cards.CardObjective;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.fail;

public class ChooseObjectiveMove extends Move{

    private final CardObjective cardObjective;

    public ChooseObjectiveMove(String playerNick, ArrayList<GameAssertion> assertions, CardObjective cardObjective) throws IllegalArgumentException {
        super(playerNick, assertions);
        if(cardObjective == null){
            throw new IllegalArgumentException();
        }
        this.cardObjective = cardObjective;
    }


    @Override
    public void play(Game game) {
        Optional<Player> player  =  super.getPlayer(game);

        if(player.isEmpty()){
            fail("Initialization Error");
        }

        game.chooseObjective(player.get(), cardObjective);
    }
}
