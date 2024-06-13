package it.polimi.ingsw.gc28.view.gui.components;

import it.polimi.ingsw.gc28.model.Cell;
import it.polimi.ingsw.gc28.model.Coordinate;
import it.polimi.ingsw.gc28.model.Table;
import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.PrivateRepresentation;
import it.polimi.ingsw.gc28.view.utils.GuiCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

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

    private double imgWidth = 180;

    private double offset = 32;

    private double imgHeight = imgWidth / 1.5;
    
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PrivateRepresentation repr = GameManagerClient.getInstance().getCurrentRepresentation().getRepresentations().get(this.selectedPlayer);
        if(repr == null){
            System.err.println("player non existent");
            return;
        }
        
        Table table = repr.getTable();
        Map<Coordinate, Cell> mapPositions = table.getMapPositions();

        showCards(mapPositions);
    }


    public TableCards(String selectedPlayer){
        HBox.setHgrow(this, Priority.ALWAYS);
        this.setStyle("-fx-background-color: red");

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


    private void showCards(Map<Coordinate, Cell> mapPositions){
        getImageViews(mapPositions);

        anchor.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("listener called");
            double width = newValue.getWidth();
            double height = newValue.getHeight();

            double centerWidth = width / 2;
            double centerHeight = height / 2;


            for(GuiCell guiCell : placedImages){
                Coordinate coord = guiCell.getCoordinate();
                double newWidth = centerWidth +  coord.getX() * (imgWidth + offset);
                double newHeight = centerHeight + coord.getY() * (imgHeight + offset);

                AnchorPane.setLeftAnchor(guiCell.getImageView(), newWidth);
                AnchorPane.setTopAnchor(guiCell.getImageView(), newHeight);
            }


            ArrayList<ImageView> images = this.placedImages.stream().map(GuiCell::getImageView).collect(Collectors.toCollection(ArrayList::new));

            anchor.getChildren().setAll(images);
        });
    }

}
