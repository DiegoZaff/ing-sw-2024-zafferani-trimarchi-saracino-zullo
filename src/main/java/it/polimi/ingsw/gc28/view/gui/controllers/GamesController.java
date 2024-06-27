package it.polimi.ingsw.gc28.view.gui.controllers;

import it.polimi.ingsw.gc28.network.messages.client.MsgJoinableGames;
import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;
import it.polimi.ingsw.gc28.network.messages.server.MessageTypeS2C;
import it.polimi.ingsw.gc28.network.messages.server.MsgOnJoinableGames;
import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.GameRepresentation;
import it.polimi.ingsw.gc28.view.GuiObserver;
import it.polimi.ingsw.gc28.view.gui.GuiApplication;
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

    public AnchorPane subView;
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

    private CreateGame createGameView;

    private JoinGame joinGameView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GameManagerClient.getInstance().addListenerAndRemoveOthers(this);
        loadSubViews();
        isCreateGameSelected = new SimpleBooleanProperty(true);
        replaceView(isCreateGameSelected.getValue());
        changeButtonWidth(createGameButton, isCreateGameSelected.getValue());

        isCreateGameSelected.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
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
        if(!isCreateGameSelected.getValue()){
            isCreateGameSelected.set(true);
        }
    }


    public void handleSelectJoinGame(MouseEvent mouseEvent) {
        if(isCreateGameSelected.getValue()){
            isCreateGameSelected.set(false);
            Platform.runLater(() -> {
                MsgJoinableGames msg = new MsgJoinableGames();
                GuiApplication.connection.sendMessageToServer(msg);
            });
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
        }else if(message.getType().equals(MessageTypeS2C.JOINABLE_GAMES)){
            joinGameView.update(message);
        }else if(message.getType().equals(MessageTypeS2C.GAME_JOINED)){
            if(wrapperController != null){
                Platform.runLater(() -> wrapperController.setInnerContent(TabType.LOBBY));
            }
        }else if(message.getType().equals(MessageTypeS2C.GAME_STARTED)){
            if(wrapperController != null){
                wrapperController.setInnerContent(TabType.IN_GAME);
            }
        }
    }



    private void loadSubViews(){
        createGameView = new CreateGame(userNameTextField);
        joinGameView = new JoinGame(userNameTextField);
    }

    private void replaceView(boolean isGameCreatedSelected){
        if(isGameCreatedSelected){
            subView.getChildren().setAll(createGameView);
        }else{
            subView.getChildren().setAll(joinGameView);
        }
    }
}
