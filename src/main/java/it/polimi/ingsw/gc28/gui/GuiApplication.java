package it.polimi.ingsw.gc28.gui;

import javafx.application.Application;
import javafx.beans.binding.DoubleBinding;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GuiApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/gc28/gui/menu.fxml"));
        Font.loadFont(getClass().getResourceAsStream("/it/polimi/ingsw/gc28/font/lexend/LexendZetta-VariableFont_wght.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/it/polimi/ingsw/gc28/font/lexend/static/LexendZetta-Bold.ttf"), 16);
        Font.loadFont(getClass().getResourceAsStream("/it/polimi/ingsw/gc28/font/lexend/static/LexendZetta-Medium.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/it/polimi/ingsw/gc28/font/lexend/static/LexendZetta-Light.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/it/polimi/ingsw/gc28/font/lexend/static/LexendZetta-Regular.ttf"), 12);

        Parent root = loader.load();
        Scene scene = new Scene(root);

        // set default font
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc28/css/application.css")).toExternalForm());

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
