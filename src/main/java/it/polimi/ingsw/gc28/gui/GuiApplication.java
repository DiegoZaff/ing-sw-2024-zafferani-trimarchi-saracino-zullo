package it.polimi.ingsw.gc28.gui;

import javafx.application.Application;
import javafx.beans.binding.DoubleBinding;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/gc28/gui/menu.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        // After loading the FXML, get the ImageView
        ImageView backgroundImageView = (ImageView) scene.lookup("#backgroundImageView");

        // Bind the fitWidth property of the ImageView to the width of the scene
        backgroundImageView.fitWidthProperty().bind(scene.widthProperty());

        stage.show();

    }

    private Parent createContent() {
        return new StackPane(new Text("Hello World"));
    }
}
