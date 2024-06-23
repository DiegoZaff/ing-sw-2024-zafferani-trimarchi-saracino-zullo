package it.polimi.ingsw.gc28.view.gui.components;

import it.polimi.ingsw.gc28.model.utils.PlayerColor;
import it.polimi.ingsw.gc28.view.GameManagerClient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Scoreboard extends VBox implements Initializable {
    @FXML
    public AnchorPane anchor;
    public ImageView scoreboard;

    @FXML
    public HBox wrapBox;

    private final Map<Integer, ArrayList<Double>> mapPointsPos = new HashMap<>(){{
        put(24, new ArrayList<>(){{
            add(76.0);
            add(40.0);
        }});
        put(25, new ArrayList<>(){{
            add(138.0);
            add(36.0);
        }});

        put(26, new ArrayList<>(){{
            add(197.0);
            add(40.0);
        }});
        put(23, new ArrayList<>(){{
            add(38.0);
            add(92.0);
        }});
        put(22, new ArrayList<>(){{
            add(38.0);
            add(156.0);
        }});
        put(21, new ArrayList<>(){{
            add(38.0);
            add(216.0);
        }});
        put(19, new ArrayList<>(){{
            add(236.0);
            add(216.0);
        }});
        put(15, new ArrayList<>(){{
            add(38.0);
            add(276.0);
        }});
        put(14, new ArrayList<>(){{
            add(38.0);
            add(337.0);
        }});
        put(7, new ArrayList<>(){{
            add(38.0);
            add(397.0);
        }});
        put(6, new ArrayList<>(){{
            add(38.0);
            add(454.0);
        }});
        put(16, new ArrayList<>(){{
            add(101.0);
            add(276.0);
        }});
        put(13, new ArrayList<>(){{
            add(101.0);
            add(337.0);
        }});
        put(8, new ArrayList<>(){{
            add(101.0);
            add(397.0);
        }});
        put(5, new ArrayList<>(){{
            add(101.0);
            add(454.0);
        }});
        put(17, new ArrayList<>(){{
            add(168.0);
            add(276.0);
        }});
        put(12, new ArrayList<>(){{
            add(168.0);
            add(337.0);
        }});
        put(9, new ArrayList<>(){{
            add(168.0);
            add(397.0);
        }});
        put(4, new ArrayList<>(){{
            add(168.0);
            add(454.0);
        }});
        put(18, new ArrayList<>(){{
            add(236.0);
            add(276.0);
        }});
        put(11, new ArrayList<>(){{
            add(236.0);
            add(337.0);
        }});
        put(10, new ArrayList<>(){{
            add(236.0);
            add(397.0);
        }});
        put(3, new ArrayList<>(){{
            add(236.0);
            add(454.0);
        }});
        put(0, new ArrayList<>(){{
            add(72.0);
            add(516.0);
        }});
        put(1, new ArrayList<>(){{
            add(136.0);
            add(516.0);
        }});
        put(2, new ArrayList<>(){{
            add(202.0);
            add(516.0);
        }});
        put(27, new ArrayList<>(){{
            add(236.0);
            add(94.0);
        }});
        put(28, new ArrayList<>(){{
            add(236.0);
            add(156.0);
        }});
        put(29, new ArrayList<>(){{
            add(137.0);
            add(105.0);
        }});
        put(20, new ArrayList<>(){{
            add(137.0);
            add(185.0);
        }});
    }};


    private final double SCOREBOARD_W = 280;
    private final double AR = 2;
    private final double SCOREBOARD_H = 280 * AR;

    private final Random random = new Random();

    private Map<String, Integer> pointsPlayers;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/gc28/img/plateau.png")));
        scoreboard.setImage(image1);
        showScoreboard();
    }

    public Map<String, Integer> getPointsPlayers() {
        return pointsPlayers;
    }

    public void showScoreboard(){

         this.pointsPlayers =  GameManagerClient.getInstance().getCurrentRepresentation().getPoints();


        for(Map.Entry<String, Integer> entry : pointsPlayers.entrySet()){
            String playerName = entry.getKey();
            Integer points = Math.min(entry.getValue(), 29);
            PlayerColor colorOfPly = GameManagerClient.getInstance().getCurrentRepresentation().getPrivateRepresentationOf(playerName).getColor();
            HBox box = getFullColoredBox(colorOfPly, 40, 40);
            ArrayList<Double> distances = mapPointsPos.get(points);
            if(distances.isEmpty()){
                System.err.println("No points tile for " + points);
                return;
            }

            int randInt1 =  random.nextInt(9) - 4;
            int randInt2 =  random.nextInt(9) - 4;

            AnchorPane.setLeftAnchor(box, distances.get(0)- 18 + randInt1);
            AnchorPane.setTopAnchor(box, distances.get(1) -18 + randInt2);

            anchor.getChildren().add(box);
        }
    }

    public Scoreboard(){
        HBox.setHgrow(this, Priority.ALWAYS);
        URL fxmlResource = ChooseCardInitial.class.getResource("/it/polimi/ingsw/gc28/gui/components/inGame/scoreboard.fxml");
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


    private HBox getFullColoredBox(PlayerColor color, double height, double width){
        VBox box = new VBox();

        box.setPrefHeight(height);
        box.setPrefWidth(width);

        String style = "-fx-background-color:" + color.getHexCodeDark() + ";-fx-background-radius: 24;-fx-border-radius: 16;";

        box.setStyle(style);

        HBox outerBox = new HBox();
        outerBox.getChildren().setAll(box);
        outerBox.setAlignment(Pos.CENTER);

        return outerBox;
    }
}
