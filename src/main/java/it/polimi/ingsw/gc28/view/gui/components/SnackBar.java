package it.polimi.ingsw.gc28.view.gui.components;

import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.InfoObserver;
import it.polimi.ingsw.gc28.view.utils.SnackBarMessage;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SnackBar extends VBox implements Initializable,InfoObserver {

    private BlockingQueue<SnackBarMessage> queue;

    private ArrayList<HBox> placedBoxes;

    @FXML
    public AnchorPane wrapper;

    public SnackBar(){
        placedBoxes = new ArrayList<>();
        queue = new LinkedBlockingQueue<>();
        URL fxmlResource = SnackBar.class.getResource("/it/polimi/ingsw/gc28/gui/components/snackbar/snackbar.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlResource);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            processIncomingMessages();
        } catch (IOException e) {
            // Component is broken, not much that can be recovered from.
            throw new RuntimeException(e);
        }
    }

    private void processIncomingMessages() {
        Platform.runLater(() -> {
            new Thread(() -> {
                while (true) {
                    try {
                        SnackBarMessage message = queue.take(); // Blocking call

                        show(message);

                    } catch (InterruptedException e) {
                        System.err.println("Thread was interrupted while taking a message!");
                        System.err.println(e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        });
    }

    @Override
    public void showInSnackBar(SnackBarMessage msg) {
        queue.add(msg);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GameManagerClient.getInstance().addSnackBarListener(this);
        wrapper.setMouseTransparent(true);
        wrapper.setPickOnBounds(true);
    }

    public void show(SnackBarMessage msg) {

        Platform.runLater(() -> {
            HBox box = createBox(msg);

            box.setOpacity(0);
            box.getStyleClass().clear();
            box.getStyleClass().addAll(msg.getStyleClass().split(" "));

            box.setTranslateY(-box.getHeight());
            wrapper.getChildren().add(box);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), box);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);

            TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), box);
            slideIn.setFromY(0);
            slideIn.setToY(16);

            for(HBox placedBox : placedBoxes){
                shiftBox(placedBox);
            }

            synchronized (this){
                placedBoxes.add(box);
            }

            fadeIn.play();
            slideIn.play();

            slideIn.setOnFinished(eventSlide -> {
                FadeTransition fadeOut = new FadeTransition(Duration.millis(500), box);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);


                fadeOut.setDelay(Duration.seconds(3));

                fadeOut.play();

                fadeOut.setOnFinished(eventFadeOut -> {
                    synchronized (this){
                        if(!placedBoxes.isEmpty()){
                            HBox boxRemoved = placedBoxes.removeFirst();
                            wrapper.getChildren().remove(boxRemoved);
                        }
                    }
                });
            });
        });
    }


    private void shiftBox(HBox box){

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(300), box);
        translateTransition.setByY(80);

        translateTransition.play();
    }

    private HBox createBox(SnackBarMessage msg){
        HBox box = new HBox();
        Insets insets = new Insets(0,24,0,24);
        box.setPadding(insets);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(36);
        imageView.setFitHeight(36);
        imageView.setCache(true);
        imageView.setCacheHint(CacheHint.SPEED);
        String base = "/it/polimi/ingsw/gc28/img/";
        String suffix = msg.getImageName();
        String path = base + suffix;
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        imageView.setImage(image);

        // Set styles
        box.getStyleClass().clear();
        box.getStyleClass().addAll(msg.getStyleClass().split(" "));

        // Create Text
        Text text = new Text(msg.getText());
        text.getStyleClass().setAll("text-style-custom");

        // Add components to the HBox
        box.getChildren().addAll(imageView, text);

        //box.setMinWidth(800);
        box.setMaxHeight(60);
        box.setMinHeight(60);
        box.setSpacing(10);
        box.setAlignment(Pos.CENTER_LEFT);
        AnchorPane.setRightAnchor(box, 0.0);

        box.toFront();
        return box;
    }
}
