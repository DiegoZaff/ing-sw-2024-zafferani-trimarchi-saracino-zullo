package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.GameRepresentation;

public class MsgOnChooseColor extends MessageS2C{

    private final String playerName;
    private final GameRepresentation gameRepresentation;

    public MsgOnChooseColor(String playerName, GameRepresentation gameRepresentation){
        super(MessageTypeS2C.CHOOSE_COLOR);
        this.playerName = playerName;
        this.gameRepresentation = gameRepresentation;
    }
    @Override
    public void update(GameManagerClient gameManagerClient, boolean isCli) {
        gameManagerClient.setCurrentRepresentation(gameRepresentation);
        String text;

        if(playerName.equals(gameManagerClient.getPlayerName())){

            text = """
                You have chosen your color:
                Don't tell anyone!
                """;

        }else{
            text = String.format("""
                %s has chosen his color!
                """, playerName);
        }
        gameManagerClient.writeInConsole(text);
    }
}
