package com.clientserver.app;

import com.clientserver.app.graphobj.GraphObject;
import com.clientserver.app.graphobj.GraphObjectFactory;
import com.clientserver.app.graphobj.GraphObjectsType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Controller {
    Model model = new Model();
    @FXML
    private Pane root;
    @FXML
    private ChoiceBox<String> choiceGraphObject;

    @FXML
    public void initialize() {
        ArrayList<String> choices = new ArrayList<>();
        for (GraphObjectsType type : GraphObjectsType.values())
            choices.add(type.toString());

        ObservableList<String> list = FXCollections.observableArrayList(choices);
        choiceGraphObject.setItems(list);
        choiceGraphObject.setValue(GraphObjectsType.CIRCLE.toString());
    }

    @FXML
    protected void onHelloButtonClick() {


    }

    @FXML
    protected void saveObjects() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text", "*.txt"), new FileChooser.ExtensionFilter("Binary", "*.bin"), new FileChooser.ExtensionFilter("XML", "*.xml"), new FileChooser.ExtensionFilter("JSON", "*.json"));
        File selectedFile = fileChooser.showSaveDialog(root.getScene().getWindow());
        if (selectedFile != null) {
            try {
                model.save(selectedFile.getPath());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @FXML
    protected void loadObjects() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load file");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text", "*.txt"), new FileChooser.ExtensionFilter("Binary", "*.bin"), new FileChooser.ExtensionFilter("XML", "*.xml"), new FileChooser.ExtensionFilter("JSON", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(root.getScene().getWindow());
        if (selectedFile != null) {
            try {
                model.load(selectedFile.getPath());
                root.getChildren().clear();
                for (GraphObject go : model.getGraphObjects()) {
                    root.getChildren().add(go.draw());
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    @FXML
    protected void onMouseButtonClick(MouseEvent event) {
        System.out.println("X - " + event.getX() + " Y - " + event.getY());

        GraphObjectsType type = GraphObjectsType.valueOf(choiceGraphObject.getValue());
        GraphObject go;
        try {
            go = GraphObjectFactory.createGraphObject(type, event.getX(), event.getY());
            root.getChildren().add(go.draw());
            model.addGrObject(go);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}