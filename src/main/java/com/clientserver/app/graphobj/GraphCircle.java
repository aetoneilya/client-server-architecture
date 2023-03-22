package com.clientserver.app.graphobj;

import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.shape.Circle;

import java.io.Serializable;


public class GraphCircle extends GraphObject implements Serializable {
    transient Circle circle;
    transient TranslateTransition transition;
    double radius;
    double centerX, centerY;


    public GraphCircle(double x, double y, double radius) {
        super(x - radius, y - radius, radius * 2, radius * 2);
        this.radius = radius;
        this.centerX = x;
        this.centerY = y;
        buildNode();
        buildTransition();
    }

    protected void buildNode() {
        circle = new Circle(centerX, centerY, radius);
    }

    protected void buildTransition() {
        transition = new TranslateTransition();
        transition.setNode(circle);
        transition.setByX(250);
        transition.setCycleCount(Transition.INDEFINITE);
        transition.setAutoReverse(true);
        transition.play();
    }


    @Override
    public String toString() {
        return "GraphCircle\n" + centerX + "\n" + centerY + "\n" + radius + "\n";
    }

    @Override
    public Node draw() {
        return circle;
    }


    @Override
    public Transition getTransition() {
        return transition;
    }
}
