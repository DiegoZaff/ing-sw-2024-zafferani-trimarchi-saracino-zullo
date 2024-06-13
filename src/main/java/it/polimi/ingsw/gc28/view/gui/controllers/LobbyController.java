package it.polimi.ingsw.gc28.view.gui.controllers;

import it.polimi.ingsw.gc28.network.messages.server.MessageS2C;
import it.polimi.ingsw.gc28.network.messages.server.MessageTypeS2C;
import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.GameRepresentation;
import it.polimi.ingsw.gc28.view.GuiObserver;
import it.polimi.ingsw.gc28.view.gui.utils.TabType;
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
            setPlayersLeft();
        }else if(message.getType().equals(MessageTypeS2C.GAME_STARTED)){
            if(wrapperController != null){
                wrapperController.setInnerContent(TabType.IN_GAME);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GameManagerClient.getInstance().addListeners(this);
        setPlayersLeft();
    }

    public void setPlayersLeft(){
        GameManagerClient gameManagerClient= GameManagerClient.getInstance();
        Integer playersTotal = gameManagerClient.getNPlayers();
        Integer playersIn = gameManagerClient.getPlayersIn();
        playersLeft.setText(String.format("%d/%d",playersIn, playersTotal));

    }

    @Override
    public void setWrapperController(WrapperController wrapperController) {
        this.wrapperController = wrapperController;
    }
}
