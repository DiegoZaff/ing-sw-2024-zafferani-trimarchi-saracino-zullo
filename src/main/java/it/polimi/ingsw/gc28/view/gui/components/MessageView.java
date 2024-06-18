package it.polimi.ingsw.gc28.view.gui.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ResourceBundle;

public class MessageView extends VBox implements Initializable {
    private final Border borderBold = new Border(new BorderStroke(
            Color.web("#424242"), // Border color
            BorderStrokeStyle.SOLID, // Border style
            new CornerRadii(16), // Corner radii
            new BorderWidths(2.5) // Border widths
    ));

    private final VBox view;

    @FXML
    public Text playerName;

    @FXML
    public Text message;
    @FXML
    public Text time;

    public MessageView(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/gc28/gui/components/games/message.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try{
            view = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setMessage(String text) {
            message.setText(text);
    }
    public void setSender(String text) {
        playerName.setText(text);
    }

    public Node getView(){
        return view;
    }

    public void setTime(String text) {
        time.setText(text);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
