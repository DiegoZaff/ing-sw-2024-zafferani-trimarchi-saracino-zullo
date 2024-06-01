package it.polimi.ingsw.gc28.view.gui.controllers;

import it.polimi.ingsw.gc28.view.gui.GuiApplication;
import it.polimi.ingsw.gc28.view.gui.utils.TabType;
import it.polimi.ingsw.gc28.view.gui.utils.WrapperControllable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WrapperController implements Initializable {
    @FXML
    public ImageView backgroundImageView;
    TabType currentTab;
    @FXML
    public HBox innerContent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backgroundImageView.fitWidthProperty().bind(GuiApplication.mainScene.widthProperty());

        currentTab = TabType.GAMES;
        setInnerContent("games");

    }

    public void setInnerContent(String contentName){
        String base = "/it/polimi/ingsw/gc28/gui/";

        FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource(base + contentName + ".fxml"));

        try {
            AnchorPane node = loader.load();
            WrapperControllable controller = loader.getController();
            controller.setWrapperController(this);

            innerContent.getChildren().setAll(node);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleGoBack(MouseEvent mouseEvent) {
        switch (currentTab){
            case LOBBY -> {
                setInnerContent("games");
                currentTab = TabType.GAMES;
            }case GAMES -> {
                GuiApplication.setRootPage("menu");
            }case IN_GAME -> {
                setInnerContent("games");
                currentTab = TabType.GAMES;
            }
        }
    }
}
