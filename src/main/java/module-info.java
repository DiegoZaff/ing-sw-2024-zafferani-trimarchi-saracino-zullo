module it.polimi.ingsw.gc28 {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    requires org.jetbrains.annotations;
    requires java.rmi;


    opens it.polimi.ingsw.gc28 to javafx.fxml;
    exports it.polimi.ingsw.gc28;
    exports it.polimi.ingsw.gc28.model;
    opens it.polimi.ingsw.gc28.model to javafx.fxml;
    exports it.polimi.ingsw.gc28.model.cards;
    opens it.polimi.ingsw.gc28.model.cards to javafx.fxml;
    exports it.polimi.ingsw.gc28.model.resources;
    opens it.polimi.ingsw.gc28.model.resources to javafx.fxml;
    exports it.polimi.ingsw.gc28.model.objectives;
    opens it.polimi.ingsw.gc28.model.objectives to javafx.fxml;
    exports it.polimi.ingsw.gc28.model.objectives.positions;
    opens it.polimi.ingsw.gc28.model.objectives.positions to javafx.fxml;
    exports it.polimi.ingsw.gc28.model.objectives.positions.utils;
    opens it.polimi.ingsw.gc28.model.objectives.positions.utils to javafx.fxml;
    exports it.polimi.ingsw.gc28.model.resources.utils;
    opens it.polimi.ingsw.gc28.model.resources.utils to javafx.fxml;
    exports it.polimi.ingsw.gc28.model.actions;
    opens it.polimi.ingsw.gc28.model.actions to javafx.fxml;
    exports it.polimi.ingsw.gc28.model.errors;
    opens it.polimi.ingsw.gc28.model.errors to javafx.fxml;
    exports it.polimi.ingsw.gc28.model.challenge;
    opens it.polimi.ingsw.gc28.model.challenge to javafx.fxml;
    exports it.polimi.ingsw.gc28.model.challenge.utils;
    opens it.polimi.ingsw.gc28.model.challenge.utils to javafx.fxml;
    exports it.polimi.ingsw.gc28.model.errors.types;
    opens it.polimi.ingsw.gc28.model.errors.types to javafx.fxml;

    opens it.polimi.ingsw.gc28.network.rmi to java.rmi;

    exports it.polimi.ingsw.gc28.gui;
    opens it.polimi.ingsw.gc28.gui to javafx.fxml;

}