package it.polimi.ingsw.gc28.view.gui;

import it.polimi.ingsw.gc28.network.messages.client.MessageC2S;

public interface GuiCallable {

    void sendMessageToServer(MessageC2S message);
}
