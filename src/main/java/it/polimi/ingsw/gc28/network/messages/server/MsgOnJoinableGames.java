package it.polimi.ingsw.gc28.network.messages.server;

import it.polimi.ingsw.gc28.model.utils.JoinInfo;
import it.polimi.ingsw.gc28.view.GameManagerClient;

import java.util.ArrayList;
/**
 * message sent from the server to the client to notify the joinable games
 */
public class MsgOnJoinableGames  extends  MessageS2C{

    private final ArrayList<JoinInfo> infos;

    public MsgOnJoinableGames(ArrayList<JoinInfo> infos) {
        super(MessageTypeS2C.JOINABLE_GAMES);
        this.infos = infos;
    }

    @Override
    public void update(GameManagerClient gameManagerClient, boolean isCli) {
        if(isCli){
            StringBuilder text = new StringBuilder();
            if(infos.isEmpty()){
                text.append("There are no games to join! Why don't you create one?");
            }else{
                for(JoinInfo info : infos){
                    text.append(info.toString());
                }
            }

            gameManagerClient.writeInConsole(text.toString());
        }else{
            gameManagerClient.updateListeners(this);
        }
    }

    public ArrayList<JoinInfo> getInfos() {
        return infos;
    }
}
