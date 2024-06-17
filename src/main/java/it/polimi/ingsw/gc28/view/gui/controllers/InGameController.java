package it.polimi.ingsw.gc28.view.gui.controllers;

import it.polimi.ingsw.gc28.model.Deck;
import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.model.cards.CardObjective;
import it.polimi.ingsw.gc28.model.cards.CardResource;
import it.polimi.ingsw.gc28.model.cards.utils.ParsingHelper;
import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;
import it.polimi.ingsw.gc28.network.messages.server.MessageTypeS2C;
import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.GameRepresentation;
import it.polimi.ingsw.gc28.view.GuiObserver;
import it.polimi.ingsw.gc28.view.PrivateRepresentation;
import it.polimi.ingsw.gc28.view.gui.components.*;
import it.polimi.ingsw.gc28.view.gui.utils.WrapperControllable;
import it.polimi.ingsw.gc28.view.utils.InGameTabType;
import it.polimi.ingsw.gc28.view.utils.PlayerColorInfo;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

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

    public ArrayList<ImageView> imageViewsHand;
    public BooleanProperty isHandOneFront = new SimpleBooleanProperty(true);
    public BooleanProperty isHandTwoFront = new SimpleBooleanProperty(true);
    public BooleanProperty isHandThreeFront = new SimpleBooleanProperty(true);

    public ArrayList<BooleanProperty> isFronts;
    public Button chatButton;
    private ArrayList<CardResource> hand;

    private TableCards tableCardsComponent;
    private WrapperController wrapperController;
    private Double screenX;
    private Double screenY;

    private ImageView draggableImage;

    private final double aspectRatio = 1.5;

    private double imgWidth = 180;

    private double offset = 32;
    private double imgHeight = imgWidth / aspectRatio;
    private String draggedImageId;
    private boolean isDraggedImageFront;

    private ChatView chatViewComponent;

    private DecksComponent decksComponent;


    private InGameTabType currentTabType = InGameTabType.INITIAL_FLOW;

    @Override
    public void update(GameRepresentation gameRepresentation) {

    }

    @Override
    public void update(MessageS2C message) {
        changeContentBasedOnAction();
        PrivateRepresentation rep = GameManagerClient.getInstance().getMyPrivateRepresentation();
        if(message.getType().equals(MessageTypeS2C.CHOOSE_OBJ)){
            CardObjective cardObj = rep.getPrivateObjective();
            if(cardObj != null) {
                String id = cardObj.getId();
                cardObjective.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(ParsingHelper.idToFrontPath(id)))));
            }
        }

        if(message.getType().equals(MessageTypeS2C.PLAY_CARD) || message.getType().equals(MessageTypeS2C.DRAW_CARD)){
            ArrayList<CardResource> myNewHand = rep.getHand();

            if(!hand.equals(myNewHand)){
                showCards(myNewHand);
            }
        }
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
        }else if(act.equals(ActionType.PLAY_CARD)){
            updateDeck();
            if(currentTabType.equals(InGameTabType.INITIAL_FLOW)){
                currentTabType = InGameTabType.TABLE;
                showTable(true);
            }else if(currentTabType.equals(InGameTabType.DECKS)){
                showDecks(false);
            }
        }else if(act.equals(ActionType.DRAW_CARD)){
            updateTable();
            if(currentTabType.equals(InGameTabType.TABLE)){
                showTable(false);
            }
        }
    }

    @Override
    public void setWrapperController(WrapperController wrapperController) {
        this.wrapperController = wrapperController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imageViewsHand = new ArrayList<>(){{
            add(handOne);
            add(handTwo);
            add(handThree);
        }};

        isFronts = new ArrayList<>(){{
            add(isHandOneFront);
            add(isHandTwoFront);
            add(isHandThreeFront);
        }};
        GameManagerClient.getInstance().addListeners(this);
        showChooseColors();
        showCards();
        setHandImagesCallbacks();
    }

    private void setHandImagesCallbacks(){
        // TODO make nicer using imageViewsHand
        handOne.setOnMousePressed((event) -> {
            onImagePress(event, 0, isHandOneFront);
        });
        handTwo.setOnMousePressed((event) -> {
            onImagePress(event, 1, isHandTwoFront);
        });
        handThree.setOnMousePressed((event) -> {
            onImagePress(event, 2, isHandThreeFront);
        });
        handOne.setOnMouseDragged(event -> {
            this.moveDraggableImage(event.getSceneX(), event.getSceneY());
        });
        handTwo.setOnMouseDragged(event -> {
            this.moveDraggableImage(event.getSceneX(), event.getSceneY());
        });
        handThree.setOnMouseDragged(event -> {
            this.moveDraggableImage(event.getSceneX(), event.getSceneY());
        });
        handOne.setOnMouseReleased(event -> {
            this.release();
        });
        handTwo.setOnMouseReleased(event -> {
            this.release();
        });
        handThree.setOnMouseReleased(event -> {
            this.release();
        });
    }
    private void onImagePress(MouseEvent event, int elementId, BooleanProperty prop){

        if(elementId >= hand.size()){
            // no card at that position
            return;
        }

        String id = hand.get(elementId).getId();
        boolean isFront = prop.get();
        ImageView draggableImage = new ImageView();
        draggableImage.setFitWidth(180);
        draggableImage.setPreserveRatio(true);
        String path = isFront? ParsingHelper.idToFrontPath(id) : ParsingHelper.idToBackResGoldPath(id);
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        draggableImage.setImage(image);

        double screenWidth = wrapperController.outerAnchorPane.getWidth();
        double screenHeight = wrapperController.outerAnchorPane.getHeight();

        this.spawnDraggableImage(event.getSceneX(), event.getSceneY(),screenWidth, screenHeight,  id, isFront, draggableImage);
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
        PrivateRepresentation rep = GameManagerClient.getInstance().getMyPrivateRepresentation();
        ArrayList<CardResource> newHand = rep.getHand();

        showCards(newHand);
    }

    private void showCards(ArrayList<CardResource> newHand){
        for (int i = 0; i < 3; i ++){
            CardResource newCardI = (newHand.size() - 1) >= i ? newHand.get(i) : null;
            CardResource oldCardI = (hand != null && (hand.size() - 1 >= i)) ? hand.get(i) : null;

            if(newCardI != null && !newCardI.equals(oldCardI)){
                Image newImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(ParsingHelper.idToFrontPath(newCardI.getId()))));
                imageViewsHand.get(i).setImage(newImage);
                isFronts.get(i).set(true);
            }else{
                if(newCardI == null){
                    imageViewsHand.get(i).setImage(null);
                }
            }
        }

        hand = newHand;
    }

    private void updateTable(){
        String playerName = GameManagerClient.getInstance().getPlayerName();
        tableCardsComponent = new TableCards(playerName);
    }
    private void showTable(boolean anew){
        if(anew || tableCardsComponent == null){
            updateTable();
        }

        innerContent.getChildren().setAll(tableCardsComponent);
    }

    public void handleTablePress(MouseEvent mouseEvent) {
        switchTab(InGameTabType.TABLE);
    }

    public void handleChatPress(MouseEvent mouseEvent) {
        changeButtonWidth(chatButton, true);
        chatViewComponent = new ChatView();
        innerContent.getChildren().setAll(chatViewComponent);
    }

    public void handleScoreboardPress(MouseEvent mouseEvent) {
    }

    public void handleDecksPress(MouseEvent mouseEvent) {
        switchTab(InGameTabType.DECKS);
    }

    /**
     * handles switches between internal tabs when user clicks on a button on the left
     * @param inGameTabType destination
     */
    private void switchTab(InGameTabType inGameTabType) {
        if(currentTabType.equals(InGameTabType.INITIAL_FLOW)){
            System.out.println("cant switch now");
            return;
        }

        if(inGameTabType.equals(InGameTabType.DECKS)){
            currentTabType = inGameTabType;
            showDecks(false);
        }else if(inGameTabType.equals(InGameTabType.TABLE)){
            currentTabType = inGameTabType;
            showTable(false);
        }
        //TODO : add chat and leaderboards
    }

    private void updateDeck(){
        decksComponent = new DecksComponent();
    }

    /**
     * show the decks and global objectives
     * @param anew if true creates a new component from scratch
     */
    private void showDecks(boolean anew) {
        if(anew || decksComponent == null){
            updateDeck();
        }
        innerContent.getChildren().setAll(decksComponent);
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



    public void spawnDraggableImage(double mouseX, double mouseY, double screenX,double screenY, String id, boolean isFront, ImageView draggableImage) {
        draggableImage.toFront();
        this.screenX = screenX;
        this.screenY = screenY;
        this.draggedImageId = id;
        this.isDraggedImageFront = isFront;

        double rightDistance = getDistanceRight(mouseX); // screeX - mouseX
        double bottomDistance = getDistanceBottom(mouseY);

        this.draggableImage = draggableImage;

        AnchorPane.setBottomAnchor(this.draggableImage, bottomDistance);
        AnchorPane.setRightAnchor(this.draggableImage, rightDistance);
        outerPane.getChildren().add(this.draggableImage);
    }

    public void moveDraggableImage(double mouseX, double mouseY) {

        double rightDistance = getDistanceRight(mouseX);
        double bottomDistance = getDistanceBottom(mouseY);

        AnchorPane.setBottomAnchor(this.draggableImage,bottomDistance);
        AnchorPane.setRightAnchor(this.draggableImage, rightDistance);

        if(tableCardsComponent != null){
            tableCardsComponent.checkHighlightPosition(rightDistance, bottomDistance);
        }
    }

    public void release() {
        outerPane.getChildren().remove(this.draggableImage);

        if(tableCardsComponent != null){
            tableCardsComponent.tryPlayCard(draggedImageId, isDraggedImageFront);
        }
    }


    private double getDistanceRight(double mouseX){
        if(screenX == null){
            System.err.println("ScreenX null while dragging");
        }

        return screenX - mouseX - imgWidth / 2;
    }

    private double getDistanceBottom(double mouseY){
        if(screenY == null){
            System.err.println("ScreenY null while dragging");
        }

        return screenY - mouseY - imgHeight / 2;
    }

    private void changeButtonWidth(Button button , boolean selected){
        if(selected){
            button.setBorder(borderBold);
        }else{
            button.setBorder(null);
        }
    }
    private final Border borderBold = new Border(new BorderStroke(
            Color.web("blue"), // Border color
            BorderStrokeStyle.SOLID, // Border style
            new CornerRadii(16), // Corner radii
            new BorderWidths(2.5) // Border widths
    ));
}
