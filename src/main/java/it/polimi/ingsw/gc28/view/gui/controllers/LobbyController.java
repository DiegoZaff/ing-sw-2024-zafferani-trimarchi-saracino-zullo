package it.polimi.ingsw.gc28.view.gui.controllers;

import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;
import it.polimi.ingsw.gc28.network.messages.server.MessageTypeS2C;
import it.polimi.ingsw.gc28.network.messages.server.MsgOnSomeoneElseJoined;
import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.GameRepresentation;
import it.polimi.ingsw.gc28.view.GuiObserver;
import it.polimi.ingsw.gc28.view.gui.utils.WrapperControllable;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class LobbyController implements Initializable, GuiObserver, WrapperControllable {
    public VBox lobbyBox;
    public int currentNPlayers;
    public Label playersLeft;

    private WrapperController wrapperController;


    @Override
    public void update(GameRepresentation gameRepresentation) {

    }

    @Override
    public void update(MessageS2C message) {
        if(message.getType().equals(MessageTypeS2C.SOMEONE_ELSE_JOINED)){
            MsgOnSomeoneElseJoined msg = (MsgOnSomeoneElseJoined) message;
            Integer playersLeftNumber = msg.getPlayersLeftToJoin();
            Integer playersTotal = GameManagerClient.getInstance().getNPlayers();
            Integer playersIn = playersTotal - playersLeftNumber;
            playersLeft.setText(String.format("%s/%s", playersIn, playersTotal));

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GameManagerClient gameManagerClient= GameManagerClient.getInstance();
        gameManagerClient.addListeners(this);
        Integer playersTotal = gameManagerClient.getNPlayers();

        playersLeft.setText(String.format("1/%s", playersTotal));

    }

    @Override
    public void setWrapperController(WrapperController wrapperController) {
        this.wrapperController = wrapperController;
    }
}
