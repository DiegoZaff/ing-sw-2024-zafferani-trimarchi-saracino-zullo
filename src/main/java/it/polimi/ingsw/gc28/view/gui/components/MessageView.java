package it.polimi.ingsw.gc28.view.gui.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

    @FXML
    public VBox boxMessage;

    public MessageView(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/gc28/gui/components/inGame/messageChat.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try{
            view = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setMessage(String text) {
        String newText = text;
        if(text.length() > 40){
            newText = insertNewLine(text, 40);
        }
        message.setText(newText);
    }
    public void setSender(String text) {
        playerName.setText(text);
        playerName.setStyle("-fx-font-weight: bold;");
    }

    public Node getView(){
        return view;
    }

    public void setTime(String text) {
        time.setText(text);
    }

    public void setBorderBold(){
        boxMessage.setBorder(borderBold);
    }

    public void setMyMessage(){
        this.view.setPadding(new Insets(5,5,5,100));
        boxMessage.setStyle("-fx-background-color: #60bbce;");
    }

    public void setSomeoneElseMessage(){
        this.view.setPadding(new Insets(5,100,5,5));
        boxMessage.setStyle("-fx-background-color: #d7d779;");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.setStyle("-fx-background-color: #EDEDED;-fx-background-radius: 16;");
    }


    public static String insertNewLine(String input, int charsPerLine) {
        StringBuilder builder = new StringBuilder(input);

        for (int i = charsPerLine; i < builder.length(); i += charsPerLine + 1) {
            builder.insert(i, '\n');
        }

        return builder.toString();
    }
}
