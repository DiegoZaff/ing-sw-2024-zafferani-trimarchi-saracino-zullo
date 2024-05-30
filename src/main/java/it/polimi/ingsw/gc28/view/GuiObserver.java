package it.polimi.ingsw.gc28.view;

import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;

public interface GuiObserver {

    public void update(GameRepresentation gameRepresentation);

    public void update(MessageS2C message);
}
