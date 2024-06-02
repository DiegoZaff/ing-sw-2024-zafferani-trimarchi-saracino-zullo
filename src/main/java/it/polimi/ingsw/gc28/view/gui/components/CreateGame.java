package it.polimi.ingsw.gc28.view.gui.components;

import it.polimi.ingsw.gc28.network.messages.client.MsgCreateGame;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.LinkedBlockingQueue;

import static it.polimi.ingsw.gc28.view.gui.GuiApplication.connection;

public class CreateGame extends VBox implements Initializable {

    private final Border borderBold = new Border(new BorderStroke(
            Color.web("#424242"), // Border color
            BorderStrokeStyle.SOLID, // Border style
            new CornerRadii(16), // Corner radii
            new BorderWidths(2.5) // Border widths
    ));
    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final int FOUR = 4;
    private IntegerProperty numberOfPlayers;
    @FXML
    Button TwoPlayersButton;
    @FXML
    Button ThreePlayersButton;
    @FXML
    Button FourPlayersButton;

    private final StringProperty userName = new SimpleStringProperty();
    public CreateGame(TextField usernameTextField){
        userName.bind(usernameTextField.textProperty());
        URL fxmlResource = SnackBar.class.getResource("/it/polimi/ingsw/gc28/gui/components/games/createGame.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlResource);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            // Component is broken, not much that can be recovered from.
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        numberOfPlayers = new SimpleIntegerProperty(TWO);
        changeButtonWidth(TwoPlayersButton, numberOfPlayers.get() == TWO);


        numberOfPlayers.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number newNumber) {
                changeButtonWidth(TwoPlayersButton, numberOfPlayers.get() == TWO);
                changeButtonWidth(ThreePlayersButton, numberOfPlayers.get() == THREE);
                changeButtonWidth(FourPlayersButton, numberOfPlayers.get() == FOUR);
            }
        });
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
        new Thread(() -> {
            String playerName;
            int nPlayers;

            nPlayers = numberOfPlayers.getValue();
            playerName = userName.getValue();
            if (playerName == null || playerName.trim().isEmpty()) {
                System.err.println("Player name cannot be empty.");
                return;
            }
            //TODO: manage RMI case
            MsgCreateGame message = new MsgCreateGame(null, playerName, nPlayers, null);
            connection.sendMessageToServer(message);
        }).start();
    }


    private void changeButtonWidth(Button button ,boolean selected){
        if(selected){
            button.setBorder(borderBold);
        }else{
            button.setBorder(null);
        }
    }

}
