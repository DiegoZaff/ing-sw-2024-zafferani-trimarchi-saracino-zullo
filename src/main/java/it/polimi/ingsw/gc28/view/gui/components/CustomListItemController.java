package it.polimi.ingsw.gc28.view.gui.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CustomListItemController extends VBox implements Initializable {

    private final Border borderBold = new Border(new BorderStroke(
            Color.web("#424242"), // Border color
            BorderStrokeStyle.SOLID, // Border style
            new CornerRadii(16), // Corner radii
            new BorderWidths(2.5) // Border widths
    ));
    private final VBox view;
    @FXML
    public Text label;
    @FXML
    public Circle coloredCircle;
    @FXML
    public VBox playersNameContainer;
    @FXML
    public Text playersLeftTag;
    public CustomListItemController(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/gc28/gui/components/games/customCell.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try{
            view = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setLabel(String text) {
        if(!label.getText().equals(text)){
            label.setText(text);
        }
    }

    public void setColoredCircle(String color){
        Paint p = Color.valueOf(color);
        coloredCircle.setFill(p);
    }
    public void setPlayersLeftTag(String pLeft){
        playersLeftTag.setText(pLeft);
    }

    public void setPlayersNames(ArrayList<String> playersNames){
        ArrayList<String> namesIn = playersNameContainer.getChildren().stream().map((node -> {
            Text nd =  (Text) node;
            return nd.getText();
        })).collect(Collectors.toCollection(ArrayList::new));

        // id different
        if(!((namesIn.containsAll(playersNames) && playersNames.containsAll(namesIn)))){
            ArrayList<Text> texts = playersNames.stream().map(Text::new).collect(Collectors.toCollection(ArrayList::new));
            texts.forEach((text -> text.setStyle("-fx-font-size: 14;-fx-background-color: #424242")));

            playersNameContainer.getChildren().setAll(texts);
        }
    }

    public void setSelected(boolean val){
        if(val){
            view.setBorder(borderBold);
            view.setPadding(new Insets(16 - 2.5));
        }else{
            view.setBorder(null);
            view.setPadding(new Insets(16));
        }

    }
    public Text getLabel() {
        return label;
    }

    public Node getView(){
        return view;
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.setStyle("-fx-background-color: #EDEDED;-fx-background-radius: 16;");
    }
}