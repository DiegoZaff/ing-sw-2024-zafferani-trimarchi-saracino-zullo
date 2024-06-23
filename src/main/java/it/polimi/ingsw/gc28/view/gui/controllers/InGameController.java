package it.polimi.ingsw.gc28.view.gui.controllers;

import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.model.cards.CardObjective;
import it.polimi.ingsw.gc28.model.cards.CardResource;
import it.polimi.ingsw.gc28.model.cards.utils.ParsingHelper;
import it.polimi.ingsw.gc28.model.utils.PlayerColor;
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
import it.polimi.ingsw.gc28.view.utils.PlayerStatusInfo;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
    public Button scoreboardButton;
    public VBox buttonWrapper;
    private ArrayList<CardResource> hand;
    private TableCards tableCardsComponent;
    private WrapperController wrapperController;
    private Double screenX;
    private Double screenY;
    private ImageView draggableImage;
    private String draggedImageId;
    private boolean isDraggedImageFront;
    private ChatView chatViewComponent;
    private Scoreboard scoreboardComponent;
    private DecksComponent decksComponent;
    private TableCards otherPlayerTable;
    private SimpleObjectProperty<InGameTabType> currentTabType = new SimpleObjectProperty<>(InGameTabType.INITIAL_FLOW) ;
    boolean isDragStarted = false;
    public static DoubleProperty imgWidthTable = new SimpleDoubleProperty(TableCards.IMG_WIDTH_START);
    public static DoubleProperty xBiasTable = new SimpleDoubleProperty(0);
    public static DoubleProperty yBiasTable = new SimpleDoubleProperty(0);
    private StringProperty currentOtherTablePlayerName = new SimpleStringProperty(null);

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
        if(message.getType().equals(MessageTypeS2C.CHAT)){
            if(chatViewComponent == null){
                chatViewComponent = new ChatView();
            }
            chatViewComponent.update(message);
        }

        Platform.runLater(() -> {
            PlayerStatusInfo info = GameManagerClient.getInstance().getMyPlayerStatusInfo();


            if(wrapperController != null && info.getColor() != null){
                wrapperController.updatePlayerStatus(info);
            }
        });
    }

    /**
     * Change inner content based on received action
     */
    private void changeContentBasedOnAction(){
        GameRepresentation repr = GameManagerClient.getInstance().getCurrentRepresentation();
        ActionType act = repr.getActionExpected();

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
            Integer roundsLeft = repr.getRoundsLeft();
            int nPlayers = repr.getNPlayers();
            if(roundsLeft != null && roundsLeft <= nPlayers - 1){
                // only play rounds. Action before was play
                updateTable();
                updateScoreboard();
                updateCurrentVisibleTab();
            }else{
                updateDeck();
                if(currentTabType.get().equals(InGameTabType.INITIAL_FLOW)){
                    // fist time that received a PLAY_CARD action
                    showSpyButton();
                    currentTabType.set(InGameTabType.TABLE);
                    showTable(true);
                }else if(currentTabType.get().equals(InGameTabType.DECKS)){
                    //always update only decks after a draw action
                    showDecks(false);
                }
            }
        }else if(act.equals(ActionType.DRAW_CARD) || act.equals(ActionType.GAME_ENDED)){
            updateScoreboard();
            updateTable();
            updateCurrentVisibleTab();
        }
    }


    private void updateCurrentVisibleTab(){
        if (currentTabType.get().equals(InGameTabType.TABLE)){
            showTable(false);
        }else if(currentTabType.get().equals(InGameTabType.LEADERBOARDS)){
            showScoreboards();
        }else if(currentTabType.get().equals(InGameTabType.OTHER_TABLE)){
            restoreOtherPlayerTableAndUpdate();
        }
    }
    /**
     * Updates the other player's table but doesn't reset the zoom and panning already done.
     */
    private void restoreOtherPlayerTableAndUpdate() {
        if(currentOtherTablePlayerName.get() != null && otherPlayerTable != null){
            double imgWidth = otherPlayerTable.getCurrentImageWidth();
            double xBias = otherPlayerTable.getCurrentXBias();
            double yBias = otherPlayerTable.getCurrentYBias();

            otherPlayerTable = new TableCards(currentOtherTablePlayerName.get(), imgWidth, xBias, yBias, false);
            innerContent.getChildren().setAll(otherPlayerTable);
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
        GameManagerClient.getInstance().addListenerAndRemoveOthers(this);
        showChooseColors();
        showCards();
        setHandImagesCallbacks();

    }

    /**
     * Sets up buttons for showing the tables of other players.
     */
    private void showSpyButton() {
        HBox spyBox = new HBox();
        spyBox.setPadding(new Insets(16,0,16,0));
        spyBox.setAlignment(Pos.CENTER);
        spyBox.setSpacing(16);

        Text spyText = new Text("Spy Others");
        spyText.setStyle("-fx-text-fill: #424242;-fx-font-size: 20");

        ImageView imgView = new ImageView();
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/gc28/img/satellite.png")));
        imgView.setImage(img);
        imgView.setFitHeight(42);
        imgView.setPreserveRatio(true);

        spyBox.getChildren().addAll(spyText, imgView);

        buttonWrapper.getChildren().add(spyBox);

        ArrayList<String> players = GameManagerClient.getInstance().getCurrentRepresentation().getNicknames().stream().filter((name) -> !name.equals(GameManagerClient.getInstance().getPlayerName())).collect(Collectors.toCollection(ArrayList::new));

        for(String ply : players){
            Button btn = getSpyButton(ply);
            buttonWrapper.getChildren().add(btn);
        }
    }

    /**
     * Helper method to get a btn that when clicked will show the table of that player
     * @param name name of the player whose table will be shown
     */
    private Button getSpyButton(String name){
        PlayerColor color =  GameManagerClient.getInstance().getCurrentRepresentation().getPrivateRepresentationOf(name).getColor();

        Button btn = new Button();
        btn.setMinHeight(48);
        btn.setMinWidth(280);
        // opacity binding to currentOtherTablePlayerName
        BooleanBinding isNameEqual = currentOtherTablePlayerName.isEqualTo(name);
        BooleanBinding isTabOtherTable = currentTabType.isEqualTo(InGameTabType.OTHER_TABLE);
        BooleanBinding condition = Bindings.and(isNameEqual, isTabOtherTable);
        DoubleBinding opacityBinding = Bindings.when(condition).then(1.0).otherwise(0.5);

        btn.opacityProperty().bind(opacityBinding);

        btn.setStyle(String.format("-fx-background-color: %s; -fx-background-radius: 16;-fx-border-color: %s; -fx-border-width: 5px;-fx-border-radius: 16;",
                color.getHexCodeLight(), color.getHexCodeDark()));
        btn.setOnMouseClicked(event -> {
            showOtherTable(name);
        });

        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(0, 16, 0, 20));

        Text text = new Text(name);
        text.setStyle("-fx-text-fill: #424242");

        hbox.getChildren().add(text);

        btn.setGraphic(hbox);

        return btn;
    }

    private void showOtherTable(String playerName){
        if(currentTabType.get().equals(InGameTabType.INITIAL_FLOW)){
            return;
        }
        otherPlayerTable = new TableCards(playerName,null,null,null,false);
        innerContent.getChildren().setAll(otherPlayerTable);
        currentTabType.set(InGameTabType.OTHER_TABLE);
        currentOtherTablePlayerName.set(playerName);
    }

    private void setHandImagesCallbacks(){
        for(int i = 0 ; i < imageViewsHand.size(); i++){
            ImageView view = imageViewsHand.get(i);
            BooleanProperty isFrontProperty = isFronts.get(i);
            final int j = i;
            view.setOnMousePressed((event) -> {
                if(event.getButton().equals(MouseButton.PRIMARY)){
                    if(currentTabType.get().equals(InGameTabType.INITIAL_FLOW)){
                        return;
                    }
                    isDragStarted = true;
                    onImagePress(event, j, isFrontProperty);
                }else{
                    if(!isDragStarted){
                        showBack(j);
                    }
                }
            });

            view.setOnMouseDragged(event -> {
                if(event.getButton().equals(MouseButton.PRIMARY)){
                    this.moveDraggableImage(event.getSceneX(), event.getSceneY());
                }
            });

            view.setOnMouseReleased(event -> {
                if(isDragStarted){
                    this.release();
                    isDragStarted = false;
                }
            });
        }
    }
    private void onImagePress(MouseEvent event, int elementId, BooleanProperty prop){

        if(!currentTabType.get().equals(InGameTabType.TABLE)){
            return;
        }

        if(elementId >= hand.size()){
            // no card at that position
            return;
        }

        String id = hand.get(elementId).getId();
        boolean isFront = prop.get();
        ImageView draggableImage = new ImageView();
        draggableImage.setFitWidth(imgWidthTable.getValue());
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
        tableCardsComponent = new TableCards(playerName, null,null,null,true);
    }
    private void showTable(boolean anew){
        if(anew || tableCardsComponent == null){
            updateTable();
        }

        innerContent.getChildren().setAll(tableCardsComponent);
    }

    @FXML
    public void handleTablePress(MouseEvent mouseEvent) {
        switchTab(InGameTabType.TABLE);
    }
    @FXML
    public void handleChatPress(MouseEvent mouseEvent) {
        switchTab(InGameTabType.CHAT);
    }
    @FXML
    public void handleScoreboardPress(MouseEvent mouseEvent) {
        switchTab(InGameTabType.LEADERBOARDS);
    }
    @FXML
    public void handleDecksPress(MouseEvent mouseEvent) {
        switchTab(InGameTabType.DECKS);
    }

    /**
     * handles switches between internal tabs when user clicks on a button on the left
     * @param inGameTabType destination
     */
    private void switchTab(InGameTabType inGameTabType) {
        if(currentTabType.get().equals(InGameTabType.INITIAL_FLOW)){
            System.out.println("cant switch now");
            return;
        }

        if(inGameTabType.equals(InGameTabType.DECKS)){
            showDecks(false);
        }else if(inGameTabType.equals(InGameTabType.TABLE)){
            showTable(false);
        }else if(inGameTabType.equals(InGameTabType.CHAT)){
            showChat(false);
        } else if(inGameTabType.equals(InGameTabType.LEADERBOARDS)) {
            showScoreboards();
        }
        currentTabType.set(inGameTabType);

    }

    private void showScoreboards(){
        if(scoreboardComponent == null){
            updateScoreboard();
        }
        innerContent.getChildren().setAll(scoreboardComponent);
    }

    private void updateScoreboard(){
        if(scoreboardComponent != null){
            Map<String, Integer> oldMap = scoreboardComponent.getPointsPlayers();
            Map<String, Integer> newMap = GameManagerClient.getInstance().getCurrentRepresentation().getPoints();
            if(!oldMap.equals(newMap)){
                scoreboardComponent = new Scoreboard();
            }
        }else{
            scoreboardComponent = new Scoreboard();
        }
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

    private void showChat(boolean anew) {
        if(anew || chatViewComponent == null){
            updateChat();
        }
        innerContent.getChildren().setAll(chatViewComponent);
    }

    private void updateChat(){
        chatViewComponent = new ChatView();
    }

    public void handleQuitPress(MouseEvent mouseEvent) {
    }

    public void showBack(int i){
        if(hand.size() < i){
            return;
        }
        String card = hand.get(i).getId();
        BooleanProperty isFront = isFronts.get(i);
        ImageView imageView = imageViewsHand.get(i);

        if(isFront.get()) {
            String back = hand.get(i).getBackImagePath();
            if (card.startsWith("G")) {
                imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(back))));
            } else {
                imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(back))));
            }
            isFront.set(false);
        } else {
            imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(ParsingHelper.idToFrontPath(card)))));
            isFront.set(true);
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

        if(tableCardsComponent != null && currentTabType.get().equals(InGameTabType.TABLE)){
            tableCardsComponent.checkHighlightPosition(rightDistance, bottomDistance);
        }
    }

    public void release() {
        outerPane.getChildren().remove(this.draggableImage);

        if(tableCardsComponent != null && currentTabType.get().equals(InGameTabType.TABLE)){
            tableCardsComponent.tryPlayCard(draggedImageId, isDraggedImageFront);
        }
    }


    private double getDistanceRight(double mouseX){
        if(screenX == null){
            System.err.println("ScreenX null while dragging");
        }

        return screenX - mouseX - imgWidthTable.getValue() / 2;
    }

    private double getDistanceBottom(double mouseY){
        if(screenY == null){
            System.err.println("ScreenY null while dragging");
        }

        return screenY - mouseY - (imgWidthTable.getValue() / TableCards.ASPECT_RATIO) / 2;
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
