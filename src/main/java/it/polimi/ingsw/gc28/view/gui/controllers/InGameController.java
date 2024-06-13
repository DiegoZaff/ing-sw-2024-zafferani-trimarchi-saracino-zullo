package it.polimi.ingsw.gc28.view.gui.controllers;

import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.model.cards.CardResource;
import it.polimi.ingsw.gc28.model.cards.utils.ParsingHelper;
import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;
import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.GameRepresentation;
import it.polimi.ingsw.gc28.view.GuiObserver;
import it.polimi.ingsw.gc28.view.PrivateRepresentation;
import it.polimi.ingsw.gc28.view.gui.components.ChooseCardInitial;
import it.polimi.ingsw.gc28.view.gui.components.ChooseColor;
import it.polimi.ingsw.gc28.view.gui.components.ChooseObjective;
import it.polimi.ingsw.gc28.view.gui.utils.WrapperControllable;
import it.polimi.ingsw.gc28.view.utils.PlayerColorInfo;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

public class InGameController implements Initializable, GuiObserver, WrapperControllable {

    @FXML
    public AnchorPane outerPane;
    @FXML
    public HBox innerContent;
    public HBox cards;
    public ImageView cardObjective;
    public ImageView handOne;
    public ImageView handTwo;
    public ImageView handThree;
    public BooleanProperty isHandOneFront;
    public BooleanProperty isHandTwoFront;
    public BooleanProperty isHandThreeFront;

    private WrapperController wrapperController;

    @Override
    public void update(GameRepresentation gameRepresentation) {

    }

    @Override
    public void update(MessageS2C message) {
        changeContentBasedOnAction();
    }

    private void changeContentBasedOnAction(){
        ActionType act = GameManagerClient.getInstance().getCurrentRepresentation().getActionExpected();
        if(act == null){
            return;
        }
        if (act.equals(ActionType.CHOOSE_COLOR)) {
            showChooseColors();
        }else if(act.equals(ActionType.PLAY_INITIAL_CARD)){
            showChooseCardInitial();
        }else if(act.equals(ActionType.CHOOSE_OBJ)){
            showChooseObjectives();
        }
    }

    @Override
    public void setWrapperController(WrapperController wrapperController) {
        this.wrapperController = wrapperController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GameManagerClient.getInstance().addListeners(this);
        showChooseColors();
        showCards();
    }

    private void showChooseColors(){
        Map<String, PrivateRepresentation> reprs = GameManagerClient.getInstance().getCurrentRepresentation().getRepresentations();
        ArrayList<PlayerColorInfo> infos = new ArrayList<>();
        for(Map.Entry<String, PrivateRepresentation> entry: reprs.entrySet()){
            PlayerColorInfo plyColorInfo = new PlayerColorInfo(entry.getKey(), entry.getValue().getColor());
            infos.add(plyColorInfo);
        }

        ChooseColor chooseColorComponent = new ChooseColor(infos);
        innerContent.getChildren().setAll(chooseColorComponent);
        innerContent.setVisible(true);

    }

    private void showChooseCardInitial(){
        ChooseCardInitial chooseCardInitialComponent = new ChooseCardInitial();

        innerContent.getChildren().setAll(chooseCardInitialComponent);
        innerContent.setVisible(true);

    }

    private void showChooseObjectives(){
        ChooseObjective chooseObjectiveComponent = new ChooseObjective();

        innerContent.getChildren().setAll(chooseObjectiveComponent);
        innerContent.setVisible(true);

    }

    private void showCards(){
        String playerName = GameManagerClient.getInstance().getPlayerName();
        PrivateRepresentation rep = GameManagerClient.getInstance().getCurrentRepresentation().getRepresentations().get(playerName);
        ArrayList<CardResource> hand = rep.getHand();
        String firstCard = hand.getFirst().getId();
        String secondCard = hand.get(1).getId();
        String thirdCard = hand.getLast().getId();
        isHandOneFront = new SimpleBooleanProperty(true);
        isHandTwoFront = new SimpleBooleanProperty(true);
        isHandThreeFront = new SimpleBooleanProperty(true);
        handOne.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(ParsingHelper.idToFrontPath(firstCard)))));
        handTwo.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(ParsingHelper.idToFrontPath(secondCard)))));
        handThree.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(ParsingHelper.idToFrontPath(thirdCard)))));
    }

    public void handleTablePress(MouseEvent mouseEvent) {
    }

    public void handleChatPress(MouseEvent mouseEvent) {
    }

    public void handleScoreboardPress(MouseEvent mouseEvent) {
    }

    public void handleDecksPress(MouseEvent mouseEvent) {
    }

    public void handleQuitPress(MouseEvent mouseEvent) {
    }

    public void showBackFirst(MouseEvent mouseEvent){
        String playerName = GameManagerClient.getInstance().getPlayerName();
        PrivateRepresentation rep = GameManagerClient.getInstance().getCurrentRepresentation().getRepresentations().get(playerName);
        ArrayList<CardResource> hand = rep.getHand();
        String firstCard = hand.getFirst().getId();
        if(isHandOneFront.get()) {
            String back = hand.getFirst().getBackImagePath();
            if (firstCard.startsWith("G")) {
                handOne.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(back))));
            } else {
                handOne.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(back))));
            }
            isHandOneFront.set(false);
        } else {
            handOne.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(ParsingHelper.idToFrontPath(firstCard)))));
            isHandOneFront.set(true);
        }
    }

    public void showBackSecond(MouseEvent mouseEvent) {
        String playerName = GameManagerClient.getInstance().getPlayerName();
        PrivateRepresentation rep = GameManagerClient.getInstance().getCurrentRepresentation().getRepresentations().get(playerName);
        ArrayList<CardResource> hand = rep.getHand();
        String secondCard = hand.get(1).getId();
        if(isHandTwoFront.get()) {
            String back = hand.get(1).getBackImagePath();
            if (secondCard.startsWith("G")) {
                handTwo.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(back))));
            } else {
                handTwo.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(back))));
            }
            isHandTwoFront.set(false);
        } else {
            handTwo.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(ParsingHelper.idToFrontPath(secondCard)))));
            isHandTwoFront.set(true);
        }
    }

    public void showBackThird(MouseEvent mouseEvent) {
        String playerName = GameManagerClient.getInstance().getPlayerName();
        PrivateRepresentation rep = GameManagerClient.getInstance().getCurrentRepresentation().getRepresentations().get(playerName);
        ArrayList<CardResource> hand = rep.getHand();
        String thirdCard = hand.getLast().getId();
        if(isHandThreeFront.get()) {
            String back = hand.getLast().getBackImagePath();
            if (thirdCard.startsWith("G")) {
                handThree.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(back))));
            } else {
                handThree.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(back))));
            }
            isHandThreeFront.set(false);
        } else {
            handThree.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(ParsingHelper.idToFrontPath(thirdCard)))));
            isHandThreeFront.set(true);
        }
    }
}
