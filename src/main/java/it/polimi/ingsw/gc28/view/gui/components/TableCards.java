package it.polimi.ingsw.gc28.view.gui.components;

import it.polimi.ingsw.gc28.model.Cell;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Table;
import it.polimi.ingsw.gc28.model.cards.utils.ParsingHelper;
import it.polimi.ingsw.gc28.network.messages.client.MsgPlayGameCard;
import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.PrivateRepresentation;
import it.polimi.ingsw.gc28.view.gui.GuiApplication;
import it.polimi.ingsw.gc28.view.gui.controllers.InGameController;
import it.polimi.ingsw.gc28.view.utils.GuiCell;
import it.polimi.ingsw.gc28.view.utils.*;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class TableCards extends VBox implements Initializable {
    @FXML
    public AnchorPane anchor;
    private final String selectedPlayer;
    ArrayList<GuiCell> placedImages;
    public static final double ASPECT_RATIO = 1.5;
    public static final double X_VERTEX_ASPECT_RATIO = 0.78;
    public static final double Y_VERTEX_ASPECT_RATIO = 0.59;
    public static final double IMG_WIDTH_START = 180;
    private final DoubleProperty imgWidth ;
    private final DoubleProperty imgHeight;
    private final DoubleProperty xOffset;
    private final DoubleProperty yOffset;
    private final DoubleProperty xBias;
    private final DoubleProperty yBias;
    private Double initialX;
    private Double initialY;
    private final ArrayList<TablePlayableCell> highlightableCenters;
    private SimpleObjectProperty<TablePlayableCell> highlightedCoord;
    private Rectangle yellowRectangle;
    private ArrayList<Coordinate> playableCoords;


    public Double getCurrentImageWidth(){
        return imgWidth.getValue();
    }

    public Double getCurrentXBias(){
        return xBias.getValue();
    }

    public Double getCurrentYBias(){
        return yBias.getValue();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PrivateRepresentation repr = GameManagerClient.getInstance().getCurrentRepresentation().getRepresentations().get(this.selectedPlayer);
        if(repr == null){
            System.err.println("player non existent");
            return;
        }

        // bind everything to imgWidth
        imgHeight.bind(imgWidth.divide(ASPECT_RATIO));
        xOffset.bind(imgWidth.multiply(X_VERTEX_ASPECT_RATIO));
        yOffset.bind(imgHeight.multiply(Y_VERTEX_ASPECT_RATIO));

        Table table = repr.getTable();
        Map<Coordinate, Cell> mapPositions = table.getMapPositions();
        playableCoords = table.getPlayableCoords();
        highlightedCoord = new SimpleObjectProperty<>(null);

        // Add a change listener to the property
        highlightedCoord.addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                if(yellowRectangle == null){
                    yellowRectangle = getNewYellowRectangle(newValue);
                    anchor.getChildren().add(yellowRectangle);
                }else{
                    AnchorPane.setRightAnchor(yellowRectangle, newValue.rightDistance() - imgWidth.getValue() / 2);
                    AnchorPane.setBottomAnchor(yellowRectangle, newValue.bottomDistance() - imgHeight.getValue() / 2);
                }
            }else{
                if(yellowRectangle != null){
                    anchor.getChildren().remove(yellowRectangle);
                    yellowRectangle = null;
                }
            }
        });

        showCards(mapPositions);
    }

    private Rectangle getNewYellowRectangle(TablePlayableCell pos){
        Rectangle rectangle = new Rectangle(imgWidth.getValue(), imgHeight.getValue());
        rectangle.setFill(Color.rgb(255, 255, 0, 0.5)); // RGB for yellow with 50% opacity
        rectangle.setArcWidth(20);
        rectangle.setArcHeight(20);

        AnchorPane.setRightAnchor(rectangle, pos.rightDistance() - imgWidth.getValue() / 2);
        AnchorPane.setBottomAnchor(rectangle, pos.bottomDistance() - imgHeight.getValue() / 2);
        return rectangle;
    }
    public TableCards(String selectedPlayer, Double imgWidthStart, Double xBiasStart, Double yBiasStart, boolean sync){

        if(!sync){
            imgWidth = new SimpleDoubleProperty(imgWidthStart != null ? imgWidthStart : IMG_WIDTH_START);
            xBias = new SimpleDoubleProperty( xBiasStart != null ? xBiasStart : 0);
            yBias = new SimpleDoubleProperty(  yBiasStart != null ? yBiasStart : 0);
        }else{
            imgWidth = InGameController.imgWidthTable;
            xBias = InGameController.xBiasTable;
            yBias = InGameController.yBiasTable;
        }
        imgHeight = new SimpleDoubleProperty();
        xOffset = new SimpleDoubleProperty();
        yOffset = new SimpleDoubleProperty();


        HBox.setHgrow(this, Priority.ALWAYS);

        this.highlightableCenters = new ArrayList<>();
        this.placedImages = new ArrayList<>();
        this.selectedPlayer = selectedPlayer;
        URL fxmlResource = ChooseCardInitial.class.getResource("/it/polimi/ingsw/gc28/gui/components/inGame/table.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlResource);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void getImageViews(Map<Coordinate, Cell> mapPositions){
        if(this.placedImages.isEmpty()){
            for (Map.Entry<Coordinate, Cell> entry : mapPositions.entrySet()) {
                Coordinate coord = entry.getKey();
                Cell cell = entry.getValue();

                ImageView imageView = new ImageView();
                imageView.setFitWidth(imgWidth.getValue());
                imageView.setPreserveRatio(true);

                String cardId = cell.getCard().getId();
                String path = ParsingHelper.idAndIsFrontToPath(cardId, cell.getIsPlayedFront());
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));

                imageView.setImage(image);

                GuiCell guiCell = new GuiCell(imageView, coord, cell.getOrderPlay());

                this.placedImages.add(guiCell);
            }
        }
    }


    private void showCards(Map<Coordinate, Cell> mapPositions){
        getImageViews(mapPositions);

        anchor.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            applyPositions(newValue);
        });
    }


    /**
     * This method lays every card down based on image width, coordinates, and center offset.
     */
    private void applyPositions(Bounds newValue){
        double width = newValue.getWidth();
        double height = newValue.getHeight();

        double centerWidth = width / 2;
        double centerHeight = height / 2;


        for(GuiCell guiCell : placedImages){
            Coordinate coord = guiCell.getCoordinate();
            double rightDistance = centerWidth - coord.getX() * (xOffset.getValue()) - (imgWidth.getValue() / 2) - xBias.getValue();
            double bottomDistance = centerHeight + coord.getY() * (yOffset.getValue()) - (imgHeight.getValue() / 2) - yBias.getValue();

            AnchorPane.setRightAnchor(guiCell.getImageView(), rightDistance);
            AnchorPane.setBottomAnchor(guiCell.getImageView(), bottomDistance);
        }


        ArrayList<ImageView> images = this.placedImages.stream().sorted(Comparator.comparingInt(GuiCell::getOrderPlay)).map(GuiCell::getImageView).collect(Collectors.toCollection(ArrayList::new));

        anchor.getChildren().setAll(images);

        highlightableCenters.clear();

        // TODO : remove this
        for(Coordinate coord : playableCoords){
            double rightDistance = centerWidth - coord.getX() * (xOffset.getValue()) -xBias.getValue() ;
            double bottomDistance = centerHeight + coord.getY() * (yOffset.getValue()) -yBias.getValue();

            TablePlayableCell coordCentre = new TablePlayableCell(coord, rightDistance, bottomDistance); //Double[]{rightDistance, bottomDistance};

//            Circle circle = new Circle(5); // Center at (100, 100), radius 5
//            circle.setFill(Color.RED); // Set fill color to red
//            AnchorPane.setRightAnchor(circle,rightDistance);
//            AnchorPane.setBottomAnchor(circle, bottomDistance);
//            anchor.getChildren().add(circle);

            highlightableCenters.add(coordCentre);
        }

    }


    public void checkHighlightPosition(double rightDistance, double bottomDistance) {

        bottomDistance = bottomDistance - 216 ;
        TablePlayableCell coordFound = null;
        for (TablePlayableCell coord : highlightableCenters){
            if(distance(coord.rightDistance() - imgWidth.getValue() / 2, coord.bottomDistance() - imgHeight.getValue() / 2, rightDistance, bottomDistance) < 50){
                coordFound = coord;
                break;
            }
        }
        highlightedCoord.set(coordFound);
    }

    public void tryPlayCard(String draggedImageId, boolean isDraggedImageFront) {
        if(highlightedCoord.get() != null){
            Coordinate coord = highlightedCoord.get().coord();

            Platform.runLater(() -> {
                String me = GameManagerClient.getInstance().getPlayerName();
                MsgPlayGameCard msg = new MsgPlayGameCard(me, draggedImageId, isDraggedImageFront, coord);

                GuiApplication.connection.sendMessageToServer(msg);

                highlightedCoord.set(null);
            });
        }
    }


    private double distance(double x1, double y1, double x2, double y2){
        return Math.sqrt((x1 -x2) * (x1-x2) + (y1 - y2) * (y1 - y2));
    }

    @FXML
    public void onAnchorPressed(MouseEvent event){
        initialX = event.getSceneX();
        initialY = event.getSceneY();
    }

    @FXML
    public void onMouseDragged(MouseEvent event){
        double dx = event.getSceneX() - initialX;
        double dy = event.getSceneY() - initialY;

        initialX = event.getSceneX();
        initialY = event.getSceneY();
        xBias.setValue(xBias.getValue()+dx);
        yBias.setValue(yBias.getValue()+dy);

        applyPositions(anchor.getLayoutBounds());
    }


    /**
     * called on scroll event to implement zoom of table.
     */
    @FXML
    public void onScroll(ScrollEvent scrollEvent){
        double scrollAmount = scrollEvent.getDeltaY();
        if(scrollAmount == 0){
            return;
        }
        else if(scrollAmount > 0){
            imgWidth.set(Math.min(300, imgWidth.getValue() + 10));
        }else {
            imgWidth.set(Math.max(80, imgWidth.getValue() - 10));
        }
        reapplyImageDimensions();
        applyPositions(anchor.getLayoutBounds());
    }

    private void reapplyImageDimensions() {
        for(GuiCell cell : placedImages){
            cell.getImageView().setFitWidth(imgWidth.getValue());
        }
    }
}
