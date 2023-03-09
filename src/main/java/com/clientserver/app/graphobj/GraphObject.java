package com.clientserver.app.graphobj;

import javafx.scene.paint.Color;

abstract public class GraphObj {
    private double centerX, centerY;
    private double height, width;
    private Color color;

    public GraphObj(double centerX, double centerY, double height, double width, Color color){
        this.centerX = centerX;
        this.centerY = centerY;
        this.height = height;
        this.width = width;
        this.color = color;
    }

    abstract void draw();

}
