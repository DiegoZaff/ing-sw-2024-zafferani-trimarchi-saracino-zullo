package it.polimi.ingsw.gc28.view.gui.components;

import it.polimi.ingsw.gc28.model.cards.CardResource;
import it.polimi.ingsw.gc28.model.cards.utils.ParsingHelper;
import it.polimi.ingsw.gc28.network.messages.client.MessageC2S;
import it.polimi.ingsw.gc28.network.messages.client.MsgDrawGameCard;
import it.polimi.ingsw.gc28.network.messages.client.MsgDrawnVisibleGameCard;
import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.GameRepresentation;
import it.polimi.ingsw.gc28.view.PrivateRepresentation;
import it.polimi.ingsw.gc28.view.gui.GuiApplication;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class DecksComponent extends VBox implements Initializable {

    @FXML
    public AnchorPane anchor;
    private ArrayList<String> faceUpGolds;
    private ArrayList<String> faceUpResource;
    private ArrayList<String> globalObjs;
    private String nextResourceCard;
    private String nextGoldCard;

    private ImageView resDeck;
    private ImageView goldDeck;
    private ImageView res1;
    private ImageView res2;

    private ImageView gold1;
    private ImageView gold2;
    private ImageView objDeck;
    private ImageView obj1;
    private ImageView obj2;

    private Map<String, ImageView> mapImageViews;

    private final double aspectRatio = 1.5;
    private double imgWidth = 180;
    private double imgHeight = imgWidth / aspectRatio;

    private final double scalingFactor = 1.2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        GameRepresentation repr = GameManagerClient.getInstance().getCurrentRepresentation();

        faceUpGolds = repr.getFaceUpGoldCards();
        faceUpResource = repr.getFaceUpResourceCards();
        nextResourceCard = repr.getNextResourceCard();
        nextGoldCard = repr.getNextGoldCard();
        globalObjs = repr.getGlobalObjectives();

        getImageViews();
        initializeCallbacksAndInsertImages();
        showDecks();

    }

    private void initializeCallbacksAndInsertImages() {
        for(Map.Entry<String, ImageView> entry : mapImageViews.entrySet()){
            String id = entry.getKey();
            ImageView img = entry.getValue();

            anchor.getChildren().add(img);

            if(!id.startsWith("OBJ")){
                img.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent) -> {
                    tryDrawCard(id);
                });
            }
        }
    }

    private void tryDrawCard(String id) {
        MessageC2S msg;
        String me = GameManagerClient.getInstance().getPlayerName();
        if(id.equals(nextResourceCard)){
            msg = new MsgDrawGameCard(me, false);
        }else if(id.equals(nextGoldCard)){
            msg = new MsgDrawGameCard(me, true);
        }else{
            msg = new MsgDrawnVisibleGameCard(me, id);
        }

        Platform.runLater(() -> {
            GuiApplication.connection.sendMessageToServer(msg);
        });
    }

    private void showDecks(){

        anchor.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            double width = newValue.getWidth();
            double height = newValue.getHeight();

            double centerWidth = width / 2;
            double centerHeight = height / 2;

            AnchorPane.setBottomAnchor(res1, centerHeight +  1 * imgHeight *scalingFactor - imgHeight / 2);
            AnchorPane.setRightAnchor(res1, centerWidth- imgWidth / 2);

            AnchorPane.setBottomAnchor(gold1, centerHeight - imgHeight / 2);
            AnchorPane.setRightAnchor(gold1, centerWidth - imgWidth / 2);

            AnchorPane.setBottomAnchor(obj1, centerHeight - 1 * imgHeight *scalingFactor - imgHeight / 2);
            AnchorPane.setRightAnchor(obj1, centerWidth  - imgWidth / 2);

            AnchorPane.setBottomAnchor(res2, centerHeight +  1 * imgHeight *scalingFactor - imgHeight / 2);
            AnchorPane.setRightAnchor(res2, centerWidth - 1 * imgWidth*scalingFactor - imgWidth / 2);

            AnchorPane.setBottomAnchor(gold2, centerHeight - imgHeight / 2);
            AnchorPane.setRightAnchor(gold2, centerWidth - 1 * imgWidth*scalingFactor - imgWidth / 2);

            AnchorPane.setBottomAnchor(obj2, centerHeight - 1 * imgHeight *scalingFactor - imgHeight / 2);
            AnchorPane.setRightAnchor(obj2, centerWidth - 1 * imgWidth *scalingFactor - imgWidth / 2);


            AnchorPane.setBottomAnchor(resDeck, centerHeight +  1 * imgHeight *scalingFactor - imgHeight / 2);
            AnchorPane.setRightAnchor(resDeck, centerWidth + 1 * imgWidth*scalingFactor - imgWidth / 2);

            AnchorPane.setBottomAnchor(goldDeck, centerHeight - imgHeight / 2);
            AnchorPane.setRightAnchor(goldDeck, centerWidth + 1 * imgWidth*scalingFactor - imgWidth / 2);

            AnchorPane.setBottomAnchor(objDeck, centerHeight - 1 * imgHeight *scalingFactor - imgHeight / 2);
            AnchorPane.setRightAnchor(objDeck, centerWidth + 1 * imgWidth *scalingFactor - imgWidth / 2);

        });
    }

    private void getImageViews() {
        // res cards
        String res1Id = !faceUpResource.isEmpty() ? faceUpResource.get(0) : null;
        String res2Id = faceUpResource.size() >= 2 ? faceUpResource.get(1) : null;
        String nextResId = nextResourceCard;

        res1 = createImageView(res1Id, true);
        res2 = createImageView(res2Id, true);
        resDeck = createImageView(nextResId, false);

        // gold cards
        String gold1Id = !faceUpGolds.isEmpty() ? faceUpGolds.get(0) : null;
        String gold2Id = faceUpGolds.size() >= 2 ? faceUpGolds.get(1) : null;
        String nextGoldId = nextGoldCard;

        gold1 = createImageView(gold1Id, true);
        gold2 = createImageView(gold2Id, true);
        goldDeck = createImageView(nextGoldId, false);

        //objs
        String obj1Id = !globalObjs.isEmpty() ? globalObjs.get(0) : null;
        String obj2Id = globalObjs.size() >= 2 ? globalObjs.get(1) : null;

        obj1 = createImageView(obj1Id, true);
        obj2 = createImageView(obj2Id, true);
        objDeck = createImageView(obj1Id, false);

        Map<String, ImageView> mapImageViews = new HashMap<>();

        addToMapIfNotNull(res1Id, res1, mapImageViews);
        addToMapIfNotNull(res2Id,res2,mapImageViews);
        addToMapIfNotNull(nextResId,resDeck,mapImageViews);
        addToMapIfNotNull(gold1Id,gold1,mapImageViews);
        addToMapIfNotNull(gold2Id,gold2,mapImageViews);
        addToMapIfNotNull(nextGoldId,goldDeck,mapImageViews);
        addToMapIfNotNull(obj1Id,obj1,mapImageViews);
        addToMapIfNotNull(obj2Id,obj2,mapImageViews);
        addToMapIfNotNull("OBJ",objDeck,mapImageViews);

        this.mapImageViews = mapImageViews;
    }

    private void addToMapIfNotNull(String key, ImageView view, Map<String, ImageView> map){
        if(key != null && map != null){
            map.put(key, view);
        }
    }


    private ImageView createImageView(String id, boolean isFront){
        ImageView imageView = new ImageView();
        imageView.setFitWidth(180);
        imageView.setPreserveRatio(true);


        Image newImage = getImageFromIdIsFront(id, isFront);
        imageView.setImage(newImage);

        return imageView;
    }

    private Image getImageFromIdIsFront(String id, boolean isFront){
        if(id == null){
            return null;
        }
        String path = ParsingHelper.idAndIsFrontToPath(id, isFront);
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
    }

    public DecksComponent(){
        HBox.setHgrow(this, Priority.ALWAYS);
        URL fxmlResource = ChooseCardInitial.class.getResource("/it/polimi/ingsw/gc28/gui/components/inGame/decks.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlResource);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
