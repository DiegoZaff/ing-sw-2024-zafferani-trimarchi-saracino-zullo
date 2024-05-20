package it.polimi.ingsw.gc28.view.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController  implements Initializable {

    @FXML
    TextField ipTextField;

    @FXML
    ImageView iconSocket;


    @FXML
    ImageView iconRMI;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    public void handleButtonSocket(ActionEvent actionEvent) {
        iconSocket.setVisible(true);
        iconRMI.setVisible(false);

    }

    @FXML
    public void handleButtonRMI(ActionEvent actionEvent) {
        iconSocket.setVisible(false);
        iconRMI.setVisible(true);

    }
}
