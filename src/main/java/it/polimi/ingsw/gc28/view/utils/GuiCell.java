package it.polimi.ingsw.gc28.view.utils;

import it.polimi.ingsw.gc28.model.Coordinate;
import javafx.scene.image.ImageView;

public class GuiCell {

    private ImageView imageView;

    private Coordinate coordinate;

    public GuiCell(ImageView imageView, Coordinate coordinate) {
        this.imageView = imageView;
        this.coordinate = coordinate;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public ImageView getImageView() {
        return imageView;
    }
}
