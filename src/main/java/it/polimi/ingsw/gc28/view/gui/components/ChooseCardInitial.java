package it.polimi.ingsw.gc28.view.gui.components;

import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.cards.CardInitial;
import it.polimi.ingsw.gc28.network.messages.client.MsgPlayGameCard;
import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.GameRepresentation;
import it.polimi.ingsw.gc28.view.PrivateRepresentation;
import it.polimi.ingsw.gc28.view.gui.GuiApplication;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChooseCardInitial extends VBox implements Initializable {
    @FXML
    public Text title;

    @FXML
    public HBox container;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showCardSelection();
    }

    private void showCardSelection(){
        GameManagerClient instance = GameManagerClient.getInstance();
        String playerOfTurn = instance.getCurrentRepresentation().getPlayerToPlay();
        String me = instance.getPlayerName();

        PrivateRepresentation repr = instance.getCurrentRepresentation().getRepresentations().get(playerOfTurn);
        CardInitial cardInitial = repr.getCardInitial();
        String cardId = cardInitial.getId();
        ImageView imageView1 = new ImageView();
        ImageView imageView2 = new ImageView();

        Image image1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/gc28/img/cards/fronts/" + cardId + ".png")));
        Image image2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/gc28/img/cards/backs/" + cardId + ".png")));

        VBox box1 = new VBox();
        box1.setSpacing(12);
        VBox box2 = new VBox();
        box2.setSpacing(12);


        box1.getChildren().add(imageView1);
        box2.getChildren().add(imageView2);

        imageView1.setImage(image1);
        imageView2.setImage(image2);

        imageView1.setPreserveRatio(true);
        imageView2.setPreserveRatio(true);

        imageView1.setFitWidth(180);
        imageView2.setFitWidth(180);

        if(me.equals(playerOfTurn)){
            title.setText("Place your initial card");
            imageView1.addEventHandler(MouseEvent.MOUSE_CLICKED, (event)->{
                sendChooseCardInitialMsg(true, cardId, me);
            });

            imageView2.addEventHandler(MouseEvent.MOUSE_CLICKED, (event)->{
                sendChooseCardInitialMsg(false, cardId, me);
            });

            HBox hBox1 = new HBox();
            hBox1.setAlignment(Pos.CENTER);
            Text textFront = new Text("FRONT");
            textFront.setStyle("-fx-font-size: 16;-fx-background-color: #424242");
            hBox1.getChildren().setAll(textFront);
            box1.getChildren().add(hBox1);

            HBox hBox2 = new HBox();
            hBox2.setAlignment(Pos.CENTER);
            Text textBack = new Text("BACK");
            textBack.setStyle("-fx-font-size: 16;-fx-background-color: #424242");
            hBox2.getChildren().setAll(textBack);
            box2.getChildren().add(hBox2);
        }else{
            title.setText(String.format("%s is placing his initial card", playerOfTurn));
        }

        container.getChildren().addAll(box1, box2);

    }

    private void sendChooseCardInitialMsg(boolean isFront, String cardId, String playerName){
        Platform.runLater(() ->{
            MsgPlayGameCard msg = new MsgPlayGameCard(playerName, cardId, isFront, new Coordinate(0,0));
            GuiApplication.connection.sendMessageToServer(msg);
        });
    }
    public ChooseCardInitial(){
        URL fxmlResource = ChooseCardInitial.class.getResource("/it/polimi/ingsw/gc28/gui/components/inGame/chooseCardInitial.fxml");
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
