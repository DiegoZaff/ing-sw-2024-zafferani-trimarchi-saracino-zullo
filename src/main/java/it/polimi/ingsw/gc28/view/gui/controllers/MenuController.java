package it.polimi.ingsw.gc28.view.gui.controllers;

import it.polimi.ingsw.gc28.network.rmi.RmiClient;
import it.polimi.ingsw.gc28.network.socket.ClientTCP;
import it.polimi.ingsw.gc28.view.GameManagerClient;
import it.polimi.ingsw.gc28.view.GameRepresentation;
import it.polimi.ingsw.gc28.view.GuiObserver;
import it.polimi.ingsw.gc28.view.gui.GuiApplication;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuController  implements Initializable, GuiObserver {

    private static final String DEFAULT_PORT_RMI = "8887";
    private static final String DEFAULT_PORT_SOCKET = "8886";

    @FXML
    Button playButton;

    @FXML
    TextField ipTextField;

    @FXML
    TextField portTextField;

    @FXML
    ImageView iconSocket;

    @FXML
    ImageView iconRMI;

    private final StringProperty portProperty = new SimpleStringProperty();

    private boolean isSocketSelected;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isSocketSelected = true;

        portTextField.textProperty().bindBidirectional(portProperty);

        portProperty.set(DEFAULT_PORT_SOCKET);
    }

    private void setIconSocketVisibility(boolean value) {
        iconSocket.setVisible(value);
    }

    private void setIconRmiVisibility(boolean value) {
        iconRMI.setVisible(value);
    }

    @FXML
    public void handleButtonSocket(MouseEvent mouseEvent) {

        if (isSocketSelected == false) {
            isSocketSelected = true;
            setIconSocketVisibility(isSocketSelected);
            setIconRmiVisibility(!isSocketSelected);

            changePort(isSocketSelected);
        }

    }

    private void changePort(boolean isSocket) {
        if (isSocket) {
            portProperty.set(DEFAULT_PORT_SOCKET);
        } else {
            portProperty.set(DEFAULT_PORT_RMI);
        }
    }

    @FXML
    public void handleButtonRMI(MouseEvent mouseEvent) {
        if (isSocketSelected == true) {
            isSocketSelected = false;
            setIconSocketVisibility(isSocketSelected);
            setIconRmiVisibility(!isSocketSelected);

            changePort(isSocketSelected);
        }

    }

    public void handlePlayButton(MouseEvent mouseEvent) {

        int port;
        try {
            port = Integer.parseInt(portProperty.getValue());
        } catch (NumberFormatException e) {
            // TODO : show error message
            System.err.println("Invalid port");
            return;
        }

        String ip = ipTextField.getText();
        String ipv4Pattern =
                "^((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])\\.){3}" +
                        "(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])$";
        Pattern pattern = Pattern.compile(ipv4Pattern);
        Matcher matcher = pattern.matcher(ip);

        if (matcher.matches()) {
            // ip is valid
            if (!isSocketSelected) {
                initializeRMIConn(ip, port);
            } else {
                initializeSocketConn(ip, port);
            }
        }

    }


    private void initializeRMIConn(String hostServer, int port) {
        new Thread(() -> {
            try {
                GuiApplication.connection = RmiClient.startClientRMI(false, hostServer, port);
                // TODO : make lobby page
                Platform.runLater(() -> GuiApplication.setRootPage("games"));
            } catch (RemoteException e) {
                // TODO : show snackBar error
                System.err.println("Remote exception!");
            } catch (NotBoundException e) {
                // TODO : show snackbar error
                System.err.println("lookup of 'VirtualServer' is failing!");
            }
        }).start();
    }


    private void initializeSocketConn(String hostServer, int port) {
        new Thread(() -> {
            try {
                GuiApplication.connection = ClientTCP.startClientSocket(hostServer, port, false);
                Platform.runLater(() -> GuiApplication.setRootPage("games"));
            } catch (IOException e) {
                // TODO :snackbar error?
                System.err.println("Could not establish TCP connection");
            }
        }).start();
    }

    @Override
    public void update(GameRepresentation gameRepresentation) {

    }
}


