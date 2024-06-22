package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.GameRepresentation;
import it.polimi.ingsw.gc28.view.utils.InformationType;
import it.polimi.ingsw.gc28.view.utils.SnackBarMessage;

import java.util.ArrayList;
/**
 * message sent from the server to the client to notify the winners of the game
 */
public class MsgOnGameWinners extends MessageS2C{
    private final GameRepresentation repr;
    private final ArrayList<String> players;
    public MsgOnGameWinners(ArrayList<String> players, GameRepresentation repr) {
        super(MessageTypeS2C.IS_WINNERS);
        this.repr = repr;
        this.players = players;
    }

    @Override
    public void update(GameManagerClient gameManagerClient, boolean isCli) {
        gameManagerClient.setCurrentRepresentation(repr);

        String text = "Game Ended!\n";

        if(players.isEmpty()){
            text+="No one won??";
        }else if (players.size() == 1){
            text+=String.format("The winners is %s", players.getFirst());
        }else{
            text+= String.format("The winners are %s" , String.join(", ", players));
        }

        if(isCli){
            gameManagerClient.writeInConsole(text);
        }else{
            gameManagerClient.updateListeners(this);
            SnackBarMessage msg = new SnackBarMessage(text, InformationType.GAME_INFO);
            gameManagerClient.updateSnackBarListener(msg);
        }
    }
}
