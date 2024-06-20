package it.polimi.ingsw.gc28.view.gui.components;

import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.model.utils.PlayerColor;
import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.utils.PlayerStatusInfo;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class PlayerStatus extends HBox implements Initializable {

    private Text roundsLeftTxt;

    private Text pointsTxt;
    private Text actionTxt;
    private PlayerStatusInfo info;

    private ImageView imgViewTrophy;

    private String me;

    private VBox nameWrapper;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        me = GameManagerClient.getInstance().getPlayerName();
        HBox coloredBox = ChooseColor.getColoredBox(info.getColor() != null ? info.getColor() : PlayerColor.RED, 54, 54);
        pointsTxt = new Text();
        pointsTxt.setText(String.valueOf(info.getPoints() != 0  ? info.getPoints() : ""));
        pointsTxt.setStyle("-fx-text-fill: #424242;-fx-font-weight: bold;-fx-font-size: 18");
        VBox innerBox = (VBox) coloredBox.getChildren().getFirst();
        innerBox.getChildren().setAll(pointsTxt);
        innerBox.setAlignment(Pos.CENTER);
        this.getChildren().add(coloredBox);

        Text name = new Text();
        name.setText(me);
        name.setStyle("-fx-text-fill: #424242;-fx-font-weight: 300; -fx-font-size: 24;");

        nameWrapper = new VBox();
        nameWrapper.setAlignment(Pos.CENTER);
        nameWrapper.getChildren().add(name);

        if(info.getRoundsLeft() != null){
            this.roundsLeftTxt = new Text();
            roundsLeftTxt.setText(String.format("%d turns left",info.getRoundsLeft()));
            roundsLeftTxt.setStyle("-fx-text-fill: #424242;-fx-font-weight: 300; -fx-font-size: 14;");

            nameWrapper.getChildren().add(roundsLeftTxt);
            nameWrapper.setAlignment(Pos.CENTER_LEFT);
        }

        this.getChildren().add(nameWrapper);

        HBox actionBox = new HBox();
        actionBox.setAlignment(Pos.CENTER);
        actionBox.setPadding(new Insets(0,0,0, 32));
        actionTxt = new Text();
        actionTxt.setStyle("-fx-text-fill: #424242;-fx-font-weight: bold; -fx-font-size: 24;");
        actionBox.getChildren().setAll(actionTxt);
        String textAction;

        if(info.getPlayerOfTurn().equals(me)){
            if(info.getAction().equals(ActionType.PLAY_CARD)){
                textAction = "PLAY";
            }else if(info.getAction().equals(ActionType.DRAW_CARD)){
                textAction = "DRAW";
            }else{
                textAction = "";
            }
        }else{
            textAction = "";
        }

        actionTxt.setText(textAction);
        this.getChildren().add(actionBox);

    }

    public PlayerStatus(PlayerStatusInfo info){
        this.info = info;
        URL fxmlResource = PlayerStatus.class.getResource("/it/polimi/ingsw/gc28/gui/components/wrapper/playerStatus.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlResource);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(PlayerStatusInfo info){
        String newPlayerOfTurn = info.getPlayerOfTurn();
        int newPoints = info.getPoints();
        Integer newRoundsLeft = info .getRoundsLeft();
        ActionType newAct = info.getAction();

        if(this.info.getPoints() != newPoints){
            updatePoints(newPoints);
        }

        if(info.isShowTrophy()){
            showTrophy();
        }

        if((this.info.getRoundsLeft() == null && newRoundsLeft != null) ||
                (this.info.getRoundsLeft() != null &&  newRoundsLeft != null
                        && !this.info.getRoundsLeft().equals(newRoundsLeft) )){
            updateRoundsLeft(newRoundsLeft);
        }

        if(!this.info.getAction().equals(newAct) || !this.info.getPlayerOfTurn().equals(newPlayerOfTurn)){
            updateAction(newAct,newPlayerOfTurn);
        }


        this.info = info;
    }

    private void updateAction(ActionType newAct, String playerOfTurn) {
        String textAction;
        if(playerOfTurn.equals(me)){
            if(newAct.equals(ActionType.PLAY_CARD)){
                textAction = "PLAY";
            }else if(newAct.equals(ActionType.DRAW_CARD)){
                textAction = "DRAW";
            }else{
                textAction = "";
            }
        }else{
            textAction = "";
        }
        actionTxt.setText(textAction);
    }

    private void showTrophy() {
        if(imgViewTrophy != null){
            System.err.println("already showing trophy...");
            return;
        }
        imgViewTrophy = new ImageView();
        imgViewTrophy.setFitWidth(48);
        imgViewTrophy.setPreserveRatio(true);

        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/gc28/img/trophy.png")));

        imgViewTrophy.setImage(img);
        this.getChildren().add(imgViewTrophy);
    }

    private void updateRoundsLeft(Integer newRoundsLeft) {
        if(roundsLeftTxt == null){
            roundsLeftTxt = new Text();
            roundsLeftTxt.setStyle("-fx-text-fill: #424242;-fx-font-weight: 300; -fx-font-size: 14;");
            nameWrapper.getChildren().add(roundsLeftTxt);
            nameWrapper.setAlignment(Pos.CENTER_LEFT);
        }

        roundsLeftTxt.setText(String.format("%d turns left", newRoundsLeft));
    }

    private void updatePoints(int newPoints) {
        pointsTxt.setText(newPoints != 0 ? String.valueOf(newPoints)  : "");
    }


}
