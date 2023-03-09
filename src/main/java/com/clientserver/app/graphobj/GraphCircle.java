package com.clientserver.app.graphobj;

import javafx.scene.paint.Color;

public class GrCircle extends GraphObj{
    javafx.scene.shape.Circle circle;
    public GrCircle(double x, double y, double radius){
        super(x, y, radius * 2, radius * 2, Color.AZURE);
        circle = new javafx.scene.shape.Circle(x, y, radius);
    }
    @Override
    void draw() {
    }
}
