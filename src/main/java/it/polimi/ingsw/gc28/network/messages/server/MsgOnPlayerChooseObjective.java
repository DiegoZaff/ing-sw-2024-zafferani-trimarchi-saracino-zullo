package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.GameRepresentation;

public class MsgOnPlayerChooseObjective extends MessageS2C{
    private final String playerName;

    private final GameRepresentation gameRepresentation;

    public MsgOnPlayerChooseObjective(String playerName, GameRepresentation gameRepresentation){
        super(MessageTypeS2C.CHOOSE_OBJ);
        this.playerName = playerName;
        this.gameRepresentation = gameRepresentation;
    }

    @Override
    public void update(GameManagerClient gameManagerClient)  {
        gameManagerClient.setCurrentRepresentation(gameRepresentation);

        String text;

        if(playerName.equals(gameManagerClient.getPlayerName())){

            String personalObjId;
            try {
                personalObjId = gameRepresentation.getRepresentations().get(playerName).getPrivateObjective().getId();
            }catch (Exception e){
                System.err.println("Something is wrong in the private representation!");
                throw new RuntimeException(e);
            }

            text = String.format("""
                You have chosen your objective: %s
                Don't tell anyone!
                """, personalObjId);

        }else{
            text = String.format("""
                %s has chosen his super secret objective!
                """, playerName);
        }

        gameManagerClient.writeInConsole(text);
    }
}

