package com.clientserver.app.connection;

import com.clientserver.app.Controller;
import com.clientserver.app.graphobj.GraphObject;
import com.clientserver.app.model.Model;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

public class CallbackControllerJavaFxImp implements CallbackController {
    private final Controller controller;
    private final Model model;

    public CallbackControllerJavaFxImp(Controller controller, Model model) {
        this.controller = controller;
        this.model = model;
    }

    public void setConnectionStatus(boolean status) {
        Platform.runLater(() -> controller.setConnectionStatus(status));
    }

    @Override
    public void deleteAll() {
        System.out.println("delete all call");
        model.getGraphObjects().clear();
        Platform.runLater(controller::removeAll);
    }

    @Override
    public void addObject(String json) {
        System.out.println("add object" + json);
        GraphObject go = model.fromJsonList(json).get(0);
        addObject(go);
    }

    public void addObject(GraphObject go) {
        Platform.runLater(() -> controller.addNode(go.draw()));
        model.addGrObject(go);
    }

    @Override
    public void addObjectList(String graphObjectListJson) {
        List<GraphObject> graphObjectList = model.fromJsonList(graphObjectListJson);
        graphObjectList.forEach(this::addObject);
    }

    @Override
    public String getObjectJson(String index) {
        int id = Integer.parseInt(index);
        List<GraphObject> l = new ArrayList<>();
        l.add(model.getGraphObjects().get(id));
        return model.toJsonList(l);
    }

    @Override
    public String getOjectListJson() {
        return model.toJsonList(model.getGraphObjects());
    }

    public String getObjectListJson() {
        return model.toJsonList(model.getGraphObjects());
    }

    @Override
    public int getSize() {
        return model.getGraphObjects().size();
    }

    @Override
    public void setSize(int i) {
        Platform.runLater(() -> controller.setRequestSize(i));
    }

}
