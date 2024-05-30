package it.polimi.ingsw.gc28.view.gui.controllers;

import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;
import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.GameRepresentation;
import it.polimi.ingsw.gc28.view.GuiObserver;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class LobbyController implements Initializable, GuiObserver {
    public VBox lobbyBox;
    public int currentNPlayers;

    @Override
    public void update(GameRepresentation gameRepresentation) {

    }

    @Override
    public void update(MessageS2C message) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
