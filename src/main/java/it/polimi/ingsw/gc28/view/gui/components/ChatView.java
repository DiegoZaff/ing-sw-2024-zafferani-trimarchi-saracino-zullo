package it.polimi.ingsw.gc28.view.gui.components;

import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.cards.CardInitial;
import it.polimi.ingsw.gc28.network.messages.client.MsgPlayGameCard;
import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.PrivateRepresentation;
import it.polimi.ingsw.gc28.view.gui.GuiApplication;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChatView extends VBox implements Initializable {
    @FXML
    public Text title;

    @FXML
    public HBox container;
    @FXML
    public HBox messageSpace;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showPlayersName();
        customizeMessageSpace();
    }

    private void showPlayersName(){
        GameManagerClient instance = GameManagerClient.getInstance();
        String me = instance.getPlayerName();
        ArrayList<String> nicknames = instance.getCurrentRepresentation().getNicknames();
        nicknames.remove(me);

        title.setText(String.format("Chats"));

        Button button1 = new Button();
        button1.setText("Global");
        button1.setPrefWidth(200);
        button1.setPrefHeight(40);
        button1.setStyle("-fx-background-color: #484848; -fx-background-radius: 16; -fx-text-fill: white;");
        Button button2 = new Button();
        button2.setText(nicknames.getFirst());
        button2.setPrefWidth(200);
        button2.setPrefHeight(40);
        button2.setStyle("-fx-background-color: #484848; -fx-background-radius: 16; -fx-text-fill: white;");
        VBox box1 = new VBox();
        box1.setSpacing(12);
        box1.getChildren().add(button1);
        box1.getChildren().add(button2);
        if(nicknames.size()>1){
            Button button3 = new Button();
            button3.setText(nicknames.get(1));
            button3.setPrefWidth(200);
            button3.setPrefHeight(40);
            button3.setStyle("-fx-background-color: #484848; -fx-background-radius: 16; -fx-text-fill: white;");
            box1.getChildren().add(button3);
        }
        if(nicknames.size()>2){
            Button button4 = new Button();
            button4.setText(nicknames.get(1));
            button4.setPrefWidth(200);
            button4.setPrefHeight(40);
            button4.setStyle("-fx-background-color: #484848; -fx-background-radius: 16; -fx-text-fill: white;");
            box1.getChildren().add(button4);
        }
        container.getChildren().add(box1);

    }

    private void customizeMessageSpace() {
        messageSpace.setStyle("-fx-padding: 0 80 0 0; -fx-background-color: orange;");
    }


    public ChatView(){
        URL fxmlResource = ChooseCardInitial.class.getResource("/it/polimi/ingsw/gc28/gui/components/inGame/chat.fxml");
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
}
