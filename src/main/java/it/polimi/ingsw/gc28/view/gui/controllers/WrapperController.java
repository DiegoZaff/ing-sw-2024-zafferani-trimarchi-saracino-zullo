package it.polimi.ingsw.gc28.view.gui.controllers;

import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.GameRepresentation;
import it.polimi.ingsw.gc28.view.gui.GuiApplication;
import it.polimi.ingsw.gc28.view.gui.utils.TabType;
import it.polimi.ingsw.gc28.view.gui.utils.WrapperControllable;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
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
    @FXML
    public Label labelGoBack;
    TabType currentTab;
    @FXML
    public AnchorPane innerContent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backgroundImageView.fitWidthProperty().bind(GuiApplication.mainScene.widthProperty());

        currentTab = TabType.GAMES;
        setInnerContent(TabType.GAMES);
    }

    /**
     * This method sets the content inside the wrapper, so it performs page changes.
     */
    public void setInnerContent(TabType tabType){
        String base = "/it/polimi/ingsw/gc28/gui/";
        innerContent.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(WrapperController.class.getResource(base + tabType.toString() + ".fxml"));
        currentTab = tabType;

        try {
            Node node = loader.load();
            WrapperControllable controller = loader.getController();
            controller.setWrapperController(this);

            innerContent.getChildren().setAll(node);
            updateGoBackButton(tabType);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleGoBack(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            TabType prevTab = currentTab.getPreviousTab();

            if(!prevTab.equals(currentTab)){
                GameManagerClient.getInstance().cleanAllListeners();
                if(prevTab.equals(TabType.MENU)){
                    GuiApplication.setRootPage("menu");
                }else{
                    setInnerContent(prevTab);
                    currentTab = prevTab;
                    updateGoBackButton(currentTab);
                }
            }
        });
    }


    /**
     * This method sets the correct name beside the Go Back Arrow
     */
    private void updateGoBackButton(TabType tab){
        String tabTitle = tab.getTabTitle();

        labelGoBack.setText(tabTitle);

    }
}
