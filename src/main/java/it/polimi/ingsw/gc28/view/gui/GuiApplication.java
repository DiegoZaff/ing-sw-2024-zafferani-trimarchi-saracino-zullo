package it.polimi.ingsw.gc28.view.gui;

import it.polimi.ingsw.gc28.network.rmi.VirtualView;
import javafx.application.Application;
import javafx.beans.binding.DoubleBinding;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GuiApplication extends Application {

    public static GuiCallable connection;

    public static Scene mainScene;


    @Override
    public void start(Stage stage) throws IOException {


        

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/gc28/gui/menu.fxml"));
        Font.loadFont(getClass().getResourceAsStream("/it/polimi/ingsw/gc28/font/lexend/LexendZetta-VariableFont_wght.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/it/polimi/ingsw/gc28/font/lexend/static/LexendZetta-Bold.ttf"), 16);
        Font.loadFont(getClass().getResourceAsStream("/it/polimi/ingsw/gc28/font/lexend/static/LexendZetta-Medium.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/it/polimi/ingsw/gc28/font/lexend/static/LexendZetta-Light.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/it/polimi/ingsw/gc28/font/lexend/static/LexendZetta-Regular.ttf"), 12);

        Image IMAGE = new Image("file:src/main/java/it/polimi/ingsw/gc28/view/gui/icon.png");

        stage.getIcons().add(IMAGE);

        stage.setTitle("Codex Naturalis");

        // load fxml
        Parent root = loader.load();

        //set the scene
        mainScene = new Scene(root);

        // set default font
        mainScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc28/css/application.css")).toExternalForm());

        stage.setScene(mainScene);


        //TODO : move this in menuController

        // After loading the FXML, get the ImageView
        ImageView backgroundImageView = (ImageView) mainScene.lookup("#backgroundImageView");

        // Bind the fitWidth property of the ImageView to the width of the scene
        backgroundImageView.fitWidthProperty().bind(mainScene.widthProperty());

        stage.show();
    }

    private Parent createContent() {
        return new StackPane(new Text("Hello World"));
    }


    /**
     * This method is responsible for changing page
     * @param pageName name of the fxml file (without extension)
     */
    public static void setRootPage(String pageName){
        String base = "/it/polimi/ingsw/gc28/gui/";

        FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource(base + pageName + ".fxml"));

        try {
            Parent root = loader.load();

            GuiApplication.mainScene.setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
