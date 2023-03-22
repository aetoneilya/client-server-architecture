package com.clientserver.app.graphobj;

import javafx.animation.Transition;
import javafx.scene.Node;

import java.io.Serializable;

abstract public class GraphObject implements Serializable {
    double x, y;
    double height, width;

    public GraphObject(double x, double y, double height, double width) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    }

    public abstract Transition getTransition();

    public abstract Node draw();

    protected abstract void buildNode() throws Exception;

    protected abstract void buildTransition();

    public void init() throws Exception {
        buildNode();
        buildTransition();
    }

    public double getWidth() {
        return width;
    }

    public double getY() {
        return y;
    }

    public double getHeight() {
        return height;
    }

    public double getX() {
        return x;
    }
}
