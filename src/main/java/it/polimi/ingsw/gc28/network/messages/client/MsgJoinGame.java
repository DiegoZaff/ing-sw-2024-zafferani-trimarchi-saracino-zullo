package it.polimi.ingsw.gc28.network.messages.client;

import it.polimi.ingsw.gc28.controller.GameController;
import it.polimi.ingsw.gc28.model.errors.types.FailedActionManaged;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;

import java.rmi.RemoteException;
import java.util.Random;

/**
 * message sent from the client to the server to join a game
 */
public class MsgJoinGame extends MessageC2S{
    private  VirtualView client;

    public void setClient(VirtualView client) {
        this.client = client;
    }

    public VirtualView getClient(){
        return client;
    }

    private String userName;

    public String getUserName(){
        return userName;
    }

    public MsgJoinGame(String gameId, String userName) {
        super(MessageTypeC2S.JOIN_GAME ,gameId);
        this.userName = userName;
        if(this.userName.isEmpty()){
            this.userName = generateRandomString(4);
        }
    }

    @Override
    public void execute(GameController controller) throws RemoteException, FailedActionManaged {
        boolean isSuccessful = controller.addPlayerToGame(userName, client);

        if(!isSuccessful){
            throw new FailedActionManaged("Could not join game");
        }
    }

    @Override
    public void attachVirtualView(VirtualView client){
        setClient(client);
    }

    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }
}
