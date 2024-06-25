package it.polimi.ingsw.gc28.view.gui.components;

import it.polimi.ingsw.gc28.model.chat.ChatMessage;
import it.polimi.ingsw.gc28.model.utils.JoinInfo;
import it.polimi.ingsw.gc28.model.utils.PlayerColor;
import it.polimi.ingsw.gc28.network.messages.client.MsgChatMessage;
import it.polimi.ingsw.gc28.network.messages.client.MsgJoinableGames;
import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;
import it.polimi.ingsw.gc28.network.messages.server.MsgOnChatMessage;
import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.GameRepresentation;
import it.polimi.ingsw.gc28.view.GuiObserver;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import static it.polimi.ingsw.gc28.view.gui.GuiApplication.connection;

public class ChatView extends VBox implements Initializable, GuiObserver {
    @FXML
    public Text title;

    @FXML
    public HBox container;
    @FXML
    public VBox messageSpace;
    @FXML
    public TextField messageField;
    @FXML
    public Button sendMessage;
    private ObservableList<ChatMessage> messages;
    @FXML
    private ListView<ChatMessage> listMessages;
    private ChatMessage selectedMessage;
    public String receiver = "all";

    private String currentChat = "global";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showPlayersName();
        customizeMessageSpace();
        sendMessage.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleSendMessage);
        messages = FXCollections.observableArrayList();
        listMessages.setItems(messages);
        listMessages.setCellFactory(new Callback<ListView<ChatMessage>, ListCell<ChatMessage>>() {
            @Override
            public ChatView.CustomChatMessage call(ListView<ChatMessage> listMessage) {
                return new ChatView.CustomChatMessage();
            }
        });
        listMessages.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ChatMessage>() {
            @Override
            public void changed(ObservableValue<? extends ChatMessage> observable, ChatMessage oldValue, ChatMessage newValue) {
                if (newValue != null) {
                    selectedMessage = newValue;
                }
            }
        });
    }

    private void handleSendMessage(MouseEvent mouseEvent) {
        String sender = GameManagerClient.getInstance().getPlayerName();

        ChatMessage message = null;
        if(this.receiver.equals("all")) {
            message = new ChatMessage(messageField.getText(), sender, "all", false);
        } else {
            message = new ChatMessage(messageField.getText(), sender, receiver, true);
        }
        ChatMessage finalMessage = message;
        Platform.runLater(() -> {
            MsgChatMessage msg = new MsgChatMessage(finalMessage);
            connection.sendMessageToServer(msg);
        });
        messageField.clear();
    }

    private void showPlayersName() {
        ArrayList<String> nicknames = getPlayersNicknames();

        title.setText(String.format("Chats"));

        Button button1 = new Button();
        button1.setText("Global");
        button1.setPrefWidth(200);
        button1.setPrefHeight(40);
        button1.setStyle("-fx-background-color: #484848; -fx-background-radius: 16; -fx-text-fill: white;");
        button1.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleSelectGlobalChat);
        Button button2 = new Button();
        button2.setText(nicknames.getFirst());
        button2.setPrefWidth(200);
        button2.setPrefHeight(40);
        button2.setStyle("-fx-background-color: #484848; -fx-background-radius: 16; -fx-text-fill: white;");
        button2.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleSelectFirstPlayer);
        VBox box1 = new VBox();
        box1.setSpacing(12);
        box1.getChildren().add(button1);
        box1.getChildren().add(button2);
        if (nicknames.size() > 1) {
            Button button3 = new Button();
            button3.setText(nicknames.get(1));
            button3.setPrefWidth(200);
            button3.setPrefHeight(40);
            button3.setStyle("-fx-background-color: #484848; -fx-background-radius: 16; -fx-text-fill: white;");
            button3.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleSelectSecondPlayer);
            box1.getChildren().add(button3);
        }
        if (nicknames.size() > 2) {
            Button button4 = new Button();
            button4.setText(nicknames.get(2));
            button4.setPrefWidth(200);
            button4.setPrefHeight(40);
            button4.setStyle("-fx-background-color: #484848; -fx-background-radius: 16; -fx-text-fill: white;");
            button4.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleSelectThirdPlayer);
            box1.getChildren().add(button4);
        }
        container.getChildren().add(box1);
    }

    private void handleSelectThirdPlayer(MouseEvent mouseEvent) {
        this.receiver = getPlayersNicknames().getLast();
        this.currentChat = receiver;
        int nPlayers = getPlayersNicknames().size();
        if(nPlayers > 2) this.receiver = getPlayersNicknames().getLast();
        ArrayList<ChatMessage> messages = findPrivateMessages(receiver);
        this.messages.setAll(messages);
    }

    private void handleSelectSecondPlayer(MouseEvent mouseEvent) {
        this.receiver = getPlayersNicknames().get(1);
        this.currentChat = receiver;
        int nPlayers = getPlayersNicknames().size();
        if(nPlayers > 1) this.receiver = getPlayersNicknames().get(1);
        ArrayList<ChatMessage> messages = findPrivateMessages(receiver);
        this.messages.setAll(messages);
    }

    private void handleSelectFirstPlayer(MouseEvent mouseEvent) {
        this.receiver = getPlayersNicknames().getFirst();
        this.currentChat = receiver;
        ArrayList<ChatMessage> messages = findPrivateMessages(receiver);
        this.messages.clear();
        this.messages.setAll(messages);
    }

    private void handleSelectGlobalChat(MouseEvent mouseEvent) {
        this.receiver = "all";
        this.currentChat = "global";
        ArrayList<ChatMessage> messages = takeGlobalMessages();
        this.messages.clear();
        this.messages.setAll(messages);
    }

    private ArrayList<ChatMessage> takeGlobalMessages() {
        ArrayList<ChatMessage> message = GameManagerClient.getInstance().getCurrentRepresentation().getChat().getChat();
        ArrayList<ChatMessage> globalMessages = new ArrayList<>();
        for(ChatMessage mex : message){
            if(!mex.isPrivate()){
                globalMessages.add(mex);
            }
        }
        return globalMessages;
    }

    public ArrayList<String> getPlayersNicknames(){
        GameManagerClient instance = GameManagerClient.getInstance();
        String me = instance.getPlayerName();
        ArrayList<String> nicknames = instance.getCurrentRepresentation().getNicknames();
        nicknames.remove(me);
        return nicknames;
    }

    public ArrayList<ChatMessage> findPrivateMessages(String name){
        String me = GameManagerClient.getInstance().getPlayerName();
        ArrayList<ChatMessage> message = GameManagerClient.getInstance().getCurrentRepresentation().getChat().getChat();
        ArrayList<ChatMessage> privateMessages = new ArrayList<>();
        for(ChatMessage mex : message){
            if(mex.isPrivate() && (mex.getReceiver().equals(name) && mex.getSender().equals(me) || (mex.getReceiver().equals(me) && mex.getSender().equals(name)))){
                privateMessages.add(mex);
            }
        }
        return privateMessages;
    }

    private void customizeMessageSpace() {
        messageField.prefWidthProperty().bind(messageSpace.widthProperty().subtract(32));
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/gc28/img/vector.png")));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        sendMessage.setGraphic(imageView);
        sendMessage.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
    }


    public ChatView() {
        HBox.setHgrow(this, Priority.ALWAYS);
        URL fxmlResource = ChooseCardInitial.class.getResource("/it/polimi/ingsw/gc28/gui/components/inGame/chat.fxml");
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

    @Override
    public void update(GameRepresentation gameRepresentation) {

    }

    @Override
    public void update(MessageS2C message) {
        MsgOnChatMessage msg = (MsgOnChatMessage) message;
        ArrayList<ChatMessage> mex= msg.getGameRepresentation().getChat().getChat();
        if((!msg.isPrivate()) && this.currentChat.equals("global")) {
            ArrayList<ChatMessage> messages = takeGlobalMessages();
            this.messages.clear();
            this.messages.setAll(messages);
        } else if(msg.isPrivate() && msg.getSender().equals(GameManagerClient.getInstance().getPlayerName())){
            ArrayList<ChatMessage> messages = findPrivateMessages(this.currentChat);
            this.messages.clear();
            this.messages.setAll(messages);
        } else if(msg.isPrivate() && msg.getReceiver().equals(GameManagerClient.getInstance().getPlayerName()) && msg.getSender().equals(this.currentChat)){
            ArrayList<ChatMessage> messages = findPrivateMessages(this.currentChat);
            this.messages.clear();
            this.messages.setAll(messages);
        }
    }

    static public class CustomChatMessage extends ListCell<ChatMessage> {
        private final MessageView controller = new MessageView();

        protected void updateItem(ChatMessage item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                String text = item.getText();
                String sender = item.getSender();
                String time = item.getTime();

                controller.setMessage(text);
                controller.setSender(sender);
                controller.setTime(time);
                setGraphic(controller.getView());

                if(sender.equals(GameManagerClient.getInstance().getPlayerName())){
                    PlayerColor playerColor = GameManagerClient.getInstance().getMyPrivateRepresentation().getColor();
                    String colorCode = playerColor.getHexCodeDark();
                    controller.setMyMessage(colorCode);
                } else {
                    PlayerColor color =  GameManagerClient.getInstance().getCurrentRepresentation().getPrivateRepresentationOf(sender).getColor();
                    String colorCode = color.getHexCodeDark();
                    controller.setSomeoneElseMessage(colorCode);
                }

            }
        }
    }
}
