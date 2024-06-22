package it.polimi.ingsw.gc28.view.gui.components;

import it.polimi.ingsw.gc28.model.utils.PlayerColor;
import it.polimi.ingsw.gc28.network.messages.client.MsgChooseColor;
import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.gui.GuiApplication;
import it.polimi.ingsw.gc28.view.utils.PlayerColorInfo;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class ChooseColor extends VBox implements Initializable {
    @FXML
    public HBox colorsContainer;

    @FXML
    public Text colorsTitle;

    private ArrayList<PlayerColorInfo> infos;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(infos.isEmpty()){
            return;
        }

        initializeTitleText();

        for(int i = 0; i < infos.size(); i++){
            VBox wrapperBox = new VBox();
            wrapperBox.setSpacing(16.0);

            PlayerColor clr = PlayerColor.values()[i];

            Optional<PlayerColorInfo> info = infos.stream().filter((info_i) ->info_i.getColor() != null && info_i.getColor().equals(clr)).findFirst();

            String name = null;
            if(info.isPresent()){
                name = info.get().getName();
            }

            HBox box = getColoredBox(clr, 88 ,88);
            wrapperBox.getChildren().add(box);

            if(name != null){
                Text playerNameText = new Text(name);
                playerNameText.setStyle("-fx-font-size: 16; -fx-fill: #424242");

                HBox hbox = new HBox();
                hbox.setAlignment(Pos.CENTER);
                hbox.getChildren().setAll(playerNameText);

                wrapperBox.getChildren().add(hbox);
            }else{
                box.addEventHandler(MouseEvent.MOUSE_CLICKED,(event) -> {
                    Platform.runLater(() -> {
                        MsgChooseColor msg = new MsgChooseColor(GameManagerClient.getInstance().getPlayerName(),  clr.toString());
                        GuiApplication.connection.sendMessageToServer(msg);
                    });
                });
            }

            colorsContainer.getChildren().add(wrapperBox);
        }
    }


    public ChooseColor(ArrayList<PlayerColorInfo> infos){
        this.infos = infos;
        URL fxmlResource = ChooseColor.class.getResource("/it/polimi/ingsw/gc28/gui/components/inGame/chooseColor.fxml");
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

    public static HBox getColoredBox(PlayerColor color, double height, double width){
        VBox box = new VBox();

        box.setPrefHeight(height);
        box.setPrefWidth(width);

        String style = "-fx-background-color:" + color.getHexCodeLight() + ";-fx-background-radius: 24;"+"-fx-border-color:" + color.getHexCodeDark() + "; -fx-border-width: 5px;-fx-border-radius: 16;";

        box.setStyle(style);

        HBox outerBox = new HBox();
        outerBox.getChildren().setAll(box);
        outerBox.setAlignment(Pos.CENTER);

        return outerBox;
    }

    private void initializeTitleText(){
        GameManagerClient instance = GameManagerClient.getInstance();
        String playerName = instance.getPlayerName();
        String playerOfTurn = instance.getCurrentRepresentation().getPlayerToPlay();

        String text;
        if(playerName.equals(playerOfTurn)){
            text = "Choose a color";
        }else{
            text = String.format("%s is choosing a color", playerOfTurn);
        }

        colorsTitle.setText(text);
    }
}
