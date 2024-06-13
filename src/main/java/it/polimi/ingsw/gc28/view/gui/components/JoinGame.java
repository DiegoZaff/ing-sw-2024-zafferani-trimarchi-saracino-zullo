package it.polimi.ingsw.gc28.view.gui.components;

import it.polimi.ingsw.gc28.model.utils.JoinInfo;
import it.polimi.ingsw.gc28.network.messages.client.MsgJoinGame;
import it.polimi.ingsw.gc28.network.messages.client.MsgJoinableGames;
import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;
import it.polimi.ingsw.gc28.network.messages.server.MsgOnJoinableGames;
import it.polimi.ingsw.gc28.view.GameRepresentation;
import it.polimi.ingsw.gc28.view.GuiObserver;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static it.polimi.ingsw.gc28.view.gui.GuiApplication.connection;

public class JoinGame extends HBox implements Initializable, GuiObserver {

    private final StringProperty username = new SimpleStringProperty();

    private ObservableList<JoinInfo> items;
    @FXML
    private ListView<JoinInfo> listView;

    @FXML
    public HBox refreshBox;

    private JoinInfo selectedJoin;

    private final Border borderBold = new Border(new BorderStroke(
            Color.web("#424242"), // Border color
            BorderStrokeStyle.SOLID, // Border style
            new CornerRadii(16), // Corner radii
            new BorderWidths(2.5) // Border widths
    ));

    public JoinGame(TextField usernameTextField){
        username.bind(usernameTextField.textProperty());
        URL fxmlResource = SnackBar.class.getResource("/it/polimi/ingsw/gc28/gui/components/games/joinGame.fxml");
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
        AnchorPane.setRightAnchor(this, 0.0);
        AnchorPane.setLeftAnchor(this, 0.0);
        items = FXCollections.observableArrayList();
        listView.setItems(items);
        refreshBox.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onRefresh);
        listView.setCellFactory(new Callback<ListView<JoinInfo>, ListCell<JoinInfo>>() {
            @Override
            public CustomListCell call(ListView<JoinInfo> listView) {
                return new CustomListCell();
            }
        });

        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<JoinInfo>() {
            @Override
            public void changed(ObservableValue<? extends JoinInfo> observable, JoinInfo oldValue, JoinInfo newValue) {
                if (newValue != null) {
                    selectedJoin = newValue;
                }
            }
        });
    }




    private void changeButtonWidth(Button button ,boolean selected){
        if(selected){
            button.setBorder(borderBold);
        }else{
            button.setBorder(null);
        }
    }

    @FXML
    public void handleJoinButton(MouseEvent event){
        if(selectedJoin == null || username.get() == null){
            return;
        }

        Platform.runLater(() ->{
            MsgJoinGame msg = new MsgJoinGame(selectedJoin.getGameId(), username.get());
            connection.sendMessageToServer(msg);
        });
    }

    public void onRefresh(MouseEvent event){
        Platform.runLater(() -> {
            MsgJoinableGames msg = new MsgJoinableGames();
            connection.sendMessageToServer(msg);

        });
    }


    @Override
    public void update(GameRepresentation gameRepresentation) {

    }

    @Override
    public void update(MessageS2C message) {
        MsgOnJoinableGames msg = (MsgOnJoinableGames) message;
        ArrayList<JoinInfo> infos = msg.getInfos();
        items.setAll(infos);
    }

    static public class CustomListCell extends ListCell<JoinInfo> {
        private final CustomListItemController controller = new CustomListItemController();

        @Override
        protected void updateItem(JoinInfo item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                int nPlayersIn = item.getPlayersAlreadyIn().size();
                int nPlayers = item.getnPlayers();
                controller.setLabel(item.getGameId());
                controller.setPlayersLeftTag(String.format("%d/%d", nPlayersIn, nPlayers));
                controller.setPlayersNames(item.getPlayersAlreadyIn());
                setGraphic(controller.getView());

                selectedProperty().addListener((observable, wasSelected, isNowSelected) -> {
                    if (isNowSelected) {
                        controller.setSelected(true);
                    } else {
                        controller.setSelected(false);
                    }
                });
            }
        }


    }
}
