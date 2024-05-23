package it.polimi.ingsw.gc28.view.gui.controllers;

import it.polimi.ingsw.gc28.view.gui.GuiApplication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateGameController implements Initializable {
    @FXML
    public TextField usernameTextField;
    public ImageView backgroundImageView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
    }

    public void onBackArrowClicked(MouseEvent mouseEvent) {
        //TODO: va aggiunto altro??
        GuiApplication.setRootPage("lobby");
    }
}
