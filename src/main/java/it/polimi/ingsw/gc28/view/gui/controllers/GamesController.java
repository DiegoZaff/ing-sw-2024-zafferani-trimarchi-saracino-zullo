package it.polimi.ingsw.gc28.view.gui.controllers;

import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;
import it.polimi.ingsw.gc28.network.messages.server.MessageTypeS2C;
import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.GameRepresentation;
import it.polimi.ingsw.gc28.view.GuiObserver;
import it.polimi.ingsw.gc28.view.gui.components.CreateGame;
import it.polimi.ingsw.gc28.view.gui.components.JoinGame;
import it.polimi.ingsw.gc28.view.gui.utils.TabType;
import it.polimi.ingsw.gc28.view.gui.utils.WrapperControllable;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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

public class GamesController implements Initializable, GuiObserver, WrapperControllable {

    public Pane subView;
    private WrapperController wrapperController;

    @Override
    public void setWrapperController(WrapperController wrapperController) {
        this.wrapperController = wrapperController;
    }


    private final Border borderBold = new Border(new BorderStroke(
            Color.web("#424242"), // Border color
            BorderStrokeStyle.SOLID, // Border style
            new CornerRadii(16), // Corner radii
            new BorderWidths(2.5) // Border widths
    ));

    @FXML
    public Button createGameButton;
    @FXML
    public Button joinGameButton;

    @FXML
    public TextField userNameTextField;

    private BooleanProperty isCreateGameSelected;

    private Parent createGameView;

    private Parent joinGameView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.print("initializing GamesController");
        loadSubViews();
        isCreateGameSelected = new SimpleBooleanProperty(true);
        replaceView(isCreateGameSelected.getValue());
        changeButtonWidth(createGameButton, isCreateGameSelected.getValue());
        GameManagerClient.getInstance().addListeners(this);

        isCreateGameSelected.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                System.out.println("listener!!");
                // Perform action when property changes
                changeButtonWidth(createGameButton, isCreateGameSelected.getValue());
                changeButtonWidth(joinGameButton, !isCreateGameSelected.getValue());

                replaceView(isCreateGameSelected.getValue());
            }
        });

        createGameButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleSelectCreateGame);
        joinGameButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleSelectJoinGame);

    }


    private void changeButtonWidth(Button button ,boolean selected){
        if(selected){
            button.setBorder(borderBold);
        }else{
            button.setBorder(null);
        }
    }


    public void handleSelectCreateGame(MouseEvent mouseEvent) {
        System.out.print("handle select create game");
        if(!isCreateGameSelected.getValue()){
            isCreateGameSelected.set(true);
        }
    }


    public void handleSelectJoinGame(MouseEvent mouseEvent) {
        System.out.print("handle select join game");
        if(isCreateGameSelected.getValue()){
            isCreateGameSelected.set(false);
        }
    }

    @Override
    public void update(GameRepresentation gameRepresentation) {

        if(gameRepresentation != null){
            if(wrapperController != null){
                Platform.runLater(() -> wrapperController.setInnerContent(TabType.LOBBY));
            }
        }
    }

    @Override
    public void update(MessageS2C message) {
        if(message.getType().equals(MessageTypeS2C.GAME_CREATED)){
            if(wrapperController != null){
                Platform.runLater(() -> wrapperController.setInnerContent(TabType.LOBBY));
            }
        }
    }



    private void loadSubViews(){
        System.out.println("Loading subviews");
        createGameView = new CreateGame(userNameTextField);
        joinGameView = new JoinGame(userNameTextField);
    }

    private void replaceView(boolean isGameCreatedSelected){
        System.out.print("replacing");
        if(isGameCreatedSelected){
            subView.getChildren().setAll(createGameView);
        }else{
            subView.getChildren().setAll(joinGameView);
        }
    }
}
