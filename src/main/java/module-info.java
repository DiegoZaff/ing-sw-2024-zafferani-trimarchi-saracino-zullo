module it.polimi.ingsw.gc28 {
    requires javafx.controls;
    requires javafx.fxml;


    opens it.polimi.ingsw.gc28 to javafx.fxml;
    exports it.polimi.ingsw.gc28;
}