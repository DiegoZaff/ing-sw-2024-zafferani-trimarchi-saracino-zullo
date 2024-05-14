package it.polimi.ingsw.gc28.network.messages.client;

import it.polimi.ingsw.gc28.controller.GameController;
import it.polimi.ingsw.gc28.model.chat.ChatMessage;

import java.rmi.RemoteException;

/**
 * Message send from the client to the server used to send a new chat message.
 */
public class MsgChatMessage extends MessageC2S{
    private final ChatMessage chatMessage;
    public MsgChatMessage(String gameId, ChatMessage chatMessage) {
        super(MessageTypeC2S.CHAT_MESSAGE ,gameId);
        this.chatMessage = chatMessage;
    }

    @Override
    public void execute(GameController controller) throws RemoteException {
        controller.sendMessage(chatMessage);
    }
}
