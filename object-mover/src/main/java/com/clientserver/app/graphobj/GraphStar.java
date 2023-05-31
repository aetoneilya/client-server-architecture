package com.clientserver.app.graphobj;

import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Line;

public class GraphStar extends GraphObject {
    transient Group group;
    transient TranslateTransition transition;

    int n;
    double centerX, centerY;
    double radius;

    public GraphStar(double x, double y, double radius, int n) throws Exception {
        super(x - radius, y - radius, radius * 2, radius * 2);
        this.n = n;
        this.centerX = x;
        this.centerY = y;
        this.radius = radius;
        buildNode();
        buildTransition();
    }

    protected void buildNode() throws Exception {
        if (n < 2) throw new Exception("sds");
        group = new Group();
        Line[] lines = new Line[n];
        double angle = 2 * Math.PI / n;

        double x = centerX;
        double y = centerY;
        double r = radius;
        for (int i = 0; i < n; i++) {
            double startX, startY, endX, endY;
            startX = x + r * Math.cos(angle * i);
            startY = y + r * Math.sin(angle * i);
            endX = x + r * Math.cos(angle * ((i + 2) % n));
            endY = y + r * Math.sin(angle * ((i + 2) % n));
            lines[i] = new Line(startX, startY, endX, endY);
        }
        group.getChildren().addAll(lines);
    }

    protected void buildTransition() {
        transition = new TranslateTransition();
        transition.setNode(group);
        transition.setByX(super.getWidth());
        transition.setByY(super.getHeight());
        transition.setAutoReverse(true);
        transition.setCycleCount(Transition.INDEFINITE);
        transition.play();
    }

    @Override
    public Transition getTransition() {
        return transition;
    }

    @Override
    public Node draw() {
        return group;
    }

    @Override
    public void init() throws Exception {
        buildNode();
        buildTransition();
    }

    @Override
    public String toString() {
        return "GraphStar\n" + centerX + '\n' + centerY + '\n' + radius + '\n' + n + '\n';
    }
}
