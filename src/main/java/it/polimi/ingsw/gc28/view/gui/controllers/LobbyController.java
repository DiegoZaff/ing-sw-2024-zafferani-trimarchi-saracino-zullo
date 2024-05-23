package it.polimi.ingsw.gc28.view.gui.controllers;

import it.polimi.ingsw.gc28.view.gui.GuiApplication;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class LobbyController implements Initializable {
    private final Border borderBold = new Border(new BorderStroke(
            Color.web("#424242"), // Border color
            BorderStrokeStyle.SOLID, // Border style
            new CornerRadii(16), // Corner radii
            new BorderWidths(2.5) // Border widths
    ));
    @FXML
    public TextField usernameTextField;
    @FXML
    public TextField numberOfPlayersTextField;
    public ImageView backgroundImageView;
    @FXML
    public Button createGameButton;
    @FXML
    public Button joinGameButton;

    private BooleanProperty isCreateGameSelected;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isCreateGameSelected = new SimpleBooleanProperty(true);
        backgroundImageView.fitWidthProperty().bind(GuiApplication.mainScene.widthProperty());
        changeButtonWidth(createGameButton, isCreateGameSelected.getValue());
        isCreateGameSelected.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                // Perform action when property changes
                changeButtonWidth(createGameButton, isCreateGameSelected.getValue());
                changeButtonWidth(joinGameButton, !isCreateGameSelected.getValue());
            }
        });
    }


    public void onBackArrowClicked(MouseEvent mouseEvent) {
        GuiApplication.connection.closeConnection();
        GuiApplication.connection = null;
        GuiApplication.setRootPage("menu");
    }

    private void changeButtonWidth(Button button ,boolean selected){
        if(selected){
            button.setBorder(borderBold);
        }else{
            button.setBorder(null);
        }
    }

    public void handleSelectCreateGame(MouseEvent mouseEvent) {
        if(!isCreateGameSelected.getValue()){
            isCreateGameSelected.set(true);
        }
    }

    public void handleSelectJoinGame(MouseEvent mouseEvent) {
        if(isCreateGameSelected.getValue()){
            isCreateGameSelected.set(false);
        }
    }
}
