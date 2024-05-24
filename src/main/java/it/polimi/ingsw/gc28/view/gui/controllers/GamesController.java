package it.polimi.ingsw.gc28.view.gui.controllers;

import it.polimi.ingsw.gc28.view.gui.GuiApplication;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GamesController implements Initializable {

    private final Border borderBold = new Border(new BorderStroke(
            Color.web("#424242"), // Border color
            BorderStrokeStyle.SOLID, // Border style
            new CornerRadii(16), // Corner radii
            new BorderWidths(2.5) // Border widths
    ));

    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final int FOUR = 4;
    @FXML
    public TextField usernameTextField;
    @FXML
    public TextField numberOfPlayersTextField;
    public ImageView backgroundImageView;
    @FXML
    public Button createGameButton;
    @FXML
    public Button joinGameButton;
    @FXML
    public Button TwoPlayersButton;
    @FXML
    public Button ThreePlayersButton;
    @FXML
    public Button FourPlayersButton;
    @FXML
    public VBox createGameBox;
    @FXML
    public VBox joinGameBox;
    public TextField gameIdTextField;
    public Button createButton;
    public Button joinButton;

    private BooleanProperty isCreateGameSelected;

    private IntegerProperty numberOfPlayers;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isCreateGameSelected = new SimpleBooleanProperty(true);
        numberOfPlayers = new SimpleIntegerProperty(TWO);
        backgroundImageView.fitWidthProperty().bind(GuiApplication.mainScene.widthProperty());
        changeButtonWidth(createGameButton, isCreateGameSelected.getValue());
        changeButtonWidth(TwoPlayersButton, numberOfPlayers.get() == TWO);

        joinGameBox.setVisible(false);
        isCreateGameSelected.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                // Perform action when property changes
                changeButtonWidth(createGameButton, isCreateGameSelected.getValue());
                changeButtonWidth(joinGameButton, !isCreateGameSelected.getValue());
            }
        });
        numberOfPlayers.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                changeButtonWidth(TwoPlayersButton, numberOfPlayers.get() == TWO);
                changeButtonWidth(ThreePlayersButton, numberOfPlayers.get() == THREE);
                changeButtonWidth(FourPlayersButton, numberOfPlayers.get() == FOUR);
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
        createGameBox.setVisible(true);
        joinGameBox.setVisible(false);
    }

    public void handleSelectJoinGame(MouseEvent mouseEvent) {
        if(isCreateGameSelected.getValue()){
            isCreateGameSelected.set(false);
        }
        joinGameBox.setVisible(true);
        createGameBox.setVisible(false);
    }

    public void handleSelectTwoPlayers(MouseEvent mouseEvent) {
        if(numberOfPlayers.get() != TWO){
            numberOfPlayers.set(TWO);
        }
    }
    public void handleSelectThreePlayers(MouseEvent mouseEvent) {
        if(numberOfPlayers.get() != THREE){
            numberOfPlayers.set(THREE);
        }
    }
    public void handleSelectFourPlayers(MouseEvent mouseEvent) {
        if(numberOfPlayers.get() != FOUR){
            numberOfPlayers.set(FOUR);
        }
    }

    public void handleCreateButton(MouseEvent mouseEvent) {

    }
}
