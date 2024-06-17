package it.polimi.ingsw.gc28.view.gui.components;

import it.polimi.ingsw.gc28.model.Coordinate;
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
import java.util.Objects;
import java.util.ResourceBundle;

public class Scoreboard extends VBox implements Initializable {
    @FXML
    public AnchorPane anchor;
    public ImageView scoreboard;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showScoreboard();
    }


    public void showScoreboard(){
        Image image1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/gc28/img/plateau.png")));
        scoreboard.setImage(image1);
        anchor.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            double width = newValue.getWidth();
            double height = newValue.getHeight();

            double imageViewWidth = scoreboard.getBoundsInParent().getWidth();
            double imageViewHeight = scoreboard.getBoundsInParent().getHeight();

            double centerX = (width - imageViewWidth) / 2;
            double centerY = (height - imageViewHeight) / 2;

            AnchorPane.setLeftAnchor(scoreboard, centerX);
            AnchorPane.setTopAnchor(scoreboard, centerY);
        });
    }

    public Scoreboard(){
        HBox.setHgrow(this, Priority.ALWAYS);
        URL fxmlResource = ChooseCardInitial.class.getResource("/it/polimi/ingsw/gc28/gui/components/inGame/scoreboard.fxml");
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
}
