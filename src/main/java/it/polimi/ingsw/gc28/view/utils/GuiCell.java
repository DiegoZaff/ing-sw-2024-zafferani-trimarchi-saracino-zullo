package it.polimi.ingsw.gc28.view.utils;

import it.polimi.ingsw.gc28.model.Coordinate;
import javafx.scene.image.ImageView;

public class GuiCell {

    private ImageView imageView;

    private Coordinate coordinate;

    private final int orderPlay;

    public GuiCell(ImageView imageView, Coordinate coordinate, int orderPlay) {
        this.imageView = imageView;
        this.coordinate = coordinate;
        this.orderPlay = orderPlay;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public int getOrderPlay() {
        return orderPlay;
    }
}
