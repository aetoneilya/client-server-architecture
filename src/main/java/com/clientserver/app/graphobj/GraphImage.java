package com.clientserver.app.graphobj;

import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.scene.image.ImageView;

import java.io.Serializable;

public class GraphImage extends GraphObject implements Serializable {
    transient ImageView image;
    transient ParallelTransition transition;
    String path;

    public GraphImage(double x, double y, double height, double width, String path) {
        super(x, y, height, width);
        this.path = path;
        buildNode();
        buildTransition();
    }

    protected void buildNode() {
        image = new ImageView(path);
        image.setFitWidth(width);
        image.setFitHeight(height);
        image.setX(x);
        image.setY(y);
    }

    protected void buildTransition() {
        ScaleTransition scaleX = new ScaleTransition();
        ScaleTransition scaleY = new ScaleTransition();
        scaleY.setDelay(scaleY.getDuration());
        scaleX.setByX(1.5);
        scaleY.setByY(1.5);
        scaleX.setAutoReverse(true);
        scaleY.setAutoReverse(true);
        scaleX.setCycleCount(Transition.INDEFINITE);
        scaleY.setCycleCount(Transition.INDEFINITE);

        transition = new ParallelTransition(scaleX, scaleY);
        transition.setNode(image);
        transition.play();
    }

    @Override
    public Node draw() {
        return image;
    }

    @Override
    public void init() throws Exception {
        buildNode();
        buildTransition();
    }

    @Override
    public Transition getTransition() {
        return transition;
    }

    @Override
    public String toString() {
        return "GraphImage\n" + x + '\n' + y + '\n' + height + '\n' + width + '\n' + path + '\'' + '\n';
    }
}
