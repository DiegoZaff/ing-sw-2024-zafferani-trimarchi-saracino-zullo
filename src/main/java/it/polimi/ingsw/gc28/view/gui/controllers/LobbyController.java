package it.polimi.ingsw.gc28.view.gui.controllers;

import it.polimi.ingsw.gc28.view.gui.GuiApplication;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class LobbyController implements Initializable {
    @FXML
    public TextField usernameTextField;
    public TextField numberOfPlayersTextField;
    public ImageView backgroundImageView;
    public Button createButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backgroundImageView.fitWidthProperty().bind(GuiApplication.mainScene.widthProperty());
    }

    public void onBackArrowClicked(MouseEvent mouseEvent) {
        GuiApplication.connection.closeConnection();
        GuiApplication.connection = null;
        GuiApplication.setRootPage("menu");
    }

    public void handleCreateButton(MouseEvent mouseEvent) {
        //TODO: controllare
        Platform.runLater(() -> GuiApplication.setRootPage("createGame"));
    }
}
