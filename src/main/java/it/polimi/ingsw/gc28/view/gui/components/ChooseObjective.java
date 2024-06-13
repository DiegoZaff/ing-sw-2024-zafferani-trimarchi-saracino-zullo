package it.polimi.ingsw.gc28.view.gui.components;

import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.cards.CardInitial;
import it.polimi.ingsw.gc28.model.cards.CardObjective;
import it.polimi.ingsw.gc28.network.messages.client.MsgChooseObjective;
import it.polimi.ingsw.gc28.network.messages.client.MsgPlayGameCard;
import it.polimi.ingsw.gc28.view.GameManagerClient;
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
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChooseObjective extends VBox implements Initializable {

    @FXML
    public Text title;
    @FXML
    public HBox container;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showCardSelection();
    }


    private void sendChooseCardObjectiveMsg(String cardId, String playerName){
        Platform.runLater(() ->{
            MsgChooseObjective msg = new MsgChooseObjective(playerName, cardId);
            GuiApplication.connection.sendMessageToServer(msg);
        });
    }

    public ChooseObjective(){
        URL fxmlResource = ChooseCardInitial.class.getResource("/it/polimi/ingsw/gc28/gui/components/inGame/chooseCardInitial.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlResource);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void showCardSelection(){
        GameManagerClient instance = GameManagerClient.getInstance();
        String playerOfTurn = instance.getCurrentRepresentation().getPlayerToPlay();
        String me = instance.getPlayerName();

        if(me.equals(playerOfTurn)){
            PrivateRepresentation repr = instance.getCurrentRepresentation().getRepresentations().get(playerOfTurn);
            ArrayList<CardObjective> objs = repr.getObjsToChoose();
            if(objs.size() != 2){
                System.err.println("Objs size is wrong!");
                return;
            }

            String cardId1 = objs.get(0).getId();
            String cardId2 = objs.get(1).getId();

            ImageView imageView1 = new ImageView();
            ImageView imageView2 = new ImageView();

            Image image1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/gc28/img/cards/fronts/" + cardId1 + ".png")));
            Image image2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/gc28/img/cards/fronts/" + cardId2 + ".png")));


            imageView1.setImage(image1);
            imageView2.setImage(image2);

            imageView1.setPreserveRatio(true);
            imageView2.setPreserveRatio(true);

            imageView1.setFitWidth(180);
            imageView2.setFitWidth(180);
            title.setText("Choose your objective");
            imageView1.addEventHandler(MouseEvent.MOUSE_CLICKED, (event)->{
                sendChooseCardObjectiveMsg(cardId1, me);
            });

            imageView2.addEventHandler(MouseEvent.MOUSE_CLICKED, (event)->{
                sendChooseCardObjectiveMsg(cardId2, me);
            });


            container.getChildren().addAll(imageView1, imageView2);
        }else{
            title.setText(String.format("%s is choosing his/her objective", playerOfTurn));
        }
    }
}
