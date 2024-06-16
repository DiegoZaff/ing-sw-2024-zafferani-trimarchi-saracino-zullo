package it.polimi.ingsw.gc28.view.gui.components;

import it.polimi.ingsw.gc28.model.Cell;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Table;
import it.polimi.ingsw.gc28.network.messages.client.MsgPlayGameCard;
import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.PrivateRepresentation;
import it.polimi.ingsw.gc28.view.gui.GuiApplication;
import it.polimi.ingsw.gc28.view.utils.GuiCell;
import it.polimi.ingsw.gc28.view.utils.TablePlayableCell;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class TableCards extends VBox implements Initializable {

    @FXML
    public AnchorPane anchor;
    private String selectedPlayer;

    ArrayList<GuiCell> placedImages;

    private final double aspectRatio = 1.5;

    private final double xVertexAspectRatio = 0.75;
    private final double yVertexAspectRatio = 0.559;

    private double imgWidth = 180;
    private double imgHeight = imgWidth / aspectRatio;

    private double xOffset = imgWidth * xVertexAspectRatio;
    private double yOffset = imgHeight * yVertexAspectRatio;

    private ImageView draggableImage;

    private Double anchorHeight;

    private Double screenX;
    private Double screenY;
    private Double initialX;

    private Double initialY;

    private ArrayList<TablePlayableCell> highlightableCenters;

    private SimpleObjectProperty<TablePlayableCell> highlightedCoord;

    private Rectangle yellowRectangle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PrivateRepresentation repr = GameManagerClient.getInstance().getCurrentRepresentation().getRepresentations().get(this.selectedPlayer);
        if(repr == null){
            System.err.println("player non existent");
            return;
        }
        
        Table table = repr.getTable();
        Map<Coordinate, Cell> mapPositions = table.getMapPositions();
        ArrayList<Coordinate> playableCoords = table.getPlayableCoords();
        highlightedCoord = new SimpleObjectProperty<>(null);

        // Add a change listener to the property
        highlightedCoord.addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                if(yellowRectangle == null){
                    yellowRectangle = getNewYellowRectangle(newValue);
                    anchor.getChildren().add(yellowRectangle);
                }else{
                    AnchorPane.setRightAnchor(yellowRectangle, newValue.rightDistance() - imgWidth / 2);
                    AnchorPane.setBottomAnchor(yellowRectangle, newValue.bottomDistance() - imgHeight / 2);
                }
            }else{
                if(yellowRectangle != null){
                    anchor.getChildren().remove(yellowRectangle);
                    yellowRectangle = null;
                }
            }
        });

        showCards(mapPositions, playableCoords);
    }

    private Rectangle getNewYellowRectangle(TablePlayableCell pos){
        Rectangle rectangle = new Rectangle(imgWidth, imgHeight);
        rectangle.setFill(Color.rgb(255, 255, 0, 0.5)); // RGB for yellow with 50% opacity
        rectangle.setArcWidth(20);
        rectangle.setArcHeight(20);

        AnchorPane.setRightAnchor(rectangle, pos.rightDistance() - imgWidth / 2);
        AnchorPane.setBottomAnchor(rectangle, pos.bottomDistance() - imgHeight / 2);
        return rectangle;
    }
    public TableCards(String selectedPlayer){
        HBox.setHgrow(this, Priority.ALWAYS);
        //this.setStyle("-fx-background-color: red");

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
                imageView.setFitWidth(180);
                imageView.setPreserveRatio(true);

                String cardId = cell.getCard().getId();
                String group = cell.getIsPlayedFront() ? "fronts" : "backs";
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/gc28/img/cards/" + group + "/" + cardId + ".png")));

                imageView.setImage(image);

                GuiCell guiCell = new GuiCell(imageView, coord);

                this.placedImages.add(guiCell);
            }
        }
    }


    private void showCards(Map<Coordinate, Cell> mapPositions, ArrayList<Coordinate> playableCoords){
        getImageViews(mapPositions);

        anchor.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            double width = newValue.getWidth();
            double height = newValue.getHeight();

            double centerWidth = width / 2;
            double centerHeight = height / 2;


            for(GuiCell guiCell : placedImages){
                Coordinate coord = guiCell.getCoordinate();
                double rightDistance = centerWidth - coord.getX() * (xOffset) - (imgWidth / 2);
                double bottomDistance = centerHeight + coord.getY() * (yOffset) - (imgHeight / 2);

                AnchorPane.setRightAnchor(guiCell.getImageView(), rightDistance);
                AnchorPane.setBottomAnchor(guiCell.getImageView(), bottomDistance);
            }


            ArrayList<ImageView> images = this.placedImages.stream().map(GuiCell::getImageView).collect(Collectors.toCollection(ArrayList::new));

            anchor.getChildren().setAll(images);

            highlightableCenters.clear();

            for(Coordinate coord : playableCoords){
                double rightDistance = centerWidth - coord.getX() * (xOffset)  ; // + Math.signum(coord.getX()) * (imgWidth/2)
                double bottomDistance = centerHeight + coord.getY() * (yOffset) ; // - Math.signum(coord.getY()) * (imgHeight/2)

                TablePlayableCell coordCentre = new TablePlayableCell(coord, rightDistance, bottomDistance); //Double[]{rightDistance, bottomDistance};

                Circle circle = new Circle(5); // Center at (100, 100), radius 5
                circle.setFill(Color.RED); // Set fill color to red
                AnchorPane.setRightAnchor(circle,rightDistance);
                AnchorPane.setBottomAnchor(circle, bottomDistance);
                anchor.getChildren().add(circle);

                highlightableCenters.add(coordCentre);
            }

        });
    }


    public void checkHighlightPosition(double rightDistance, double bottomDistance) {

        bottomDistance = bottomDistance - 216 ;
        TablePlayableCell coordFound = null;
        for (TablePlayableCell coord : highlightableCenters){
            if(distance(coord.rightDistance() - imgWidth / 2, coord.bottomDistance() - imgHeight / 2, rightDistance, bottomDistance) < 50){
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
}
