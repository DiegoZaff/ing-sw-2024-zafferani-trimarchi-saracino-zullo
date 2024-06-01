package it.polimi.ingsw.gc28.games.assertions;

import it.polimi.ingsw.gc28.model.Game;
import it.polimi.ingsw.gc28.model.Player;

import java.util.Objects;
import java.util.Optional;


public class ColorChosenGameAssertion extends GameAssertion{
        private String color;
        private String name;
        private String actualColor;
        public ColorChosenGameAssertion(String namePlayer, String colorChosen) {

            this.color = colorChosen;
            this.name = namePlayer;
        }

        @Override
        public boolean verifyAssertion(Game game) {
            Optional<Player> player = super.getPlayerFromNick(name, game);

            if(player.isEmpty()){
                return false;
            }
            actualColor = player.get().getColor().toString();

            return Objects.equals(color, actualColor);

        }

    @Override
    public String toString() {
        return String.format("ColorChosenGame Assertion --- expectedColor: %s,  actualColor: %s", color, actualColor);
    }
    }
