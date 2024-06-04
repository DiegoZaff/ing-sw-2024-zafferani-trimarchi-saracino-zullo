package it.polimi.ingsw.gc28.view.gui.controllers;

import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;
import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.GameRepresentation;
import it.polimi.ingsw.gc28.view.GuiObserver;
import it.polimi.ingsw.gc28.view.gui.utils.WrapperControllable;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class InGameController implements Initializable, GuiObserver, WrapperControllable {

    private WrapperController wrapperController;

    @Override
    public void update(GameRepresentation gameRepresentation) {

    }

    @Override
    public void update(MessageS2C message) {

    }

    @Override
    public void setWrapperController(WrapperController wrapperController) {
        this.wrapperController = wrapperController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GameManagerClient.getInstance().addListeners(this);
    }

    public void handleTablePress(MouseEvent mouseEvent) {
    }

    public void handleChatPress(MouseEvent mouseEvent) {
    }

    public void handleScoreboardPress(MouseEvent mouseEvent) {
    }

    public void handleDecksPress(MouseEvent mouseEvent) {
    }

    public void handleQuitPress(MouseEvent mouseEvent) {
    }
}
