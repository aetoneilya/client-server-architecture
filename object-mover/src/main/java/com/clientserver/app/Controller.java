package com.clientserver.app;

import com.clientserver.app.connection.CallbackController;
import com.clientserver.app.connection.CallbackControllerJavaFxImp;
import com.clientserver.app.connection.NetworkConnector;
import com.clientserver.app.connection.http.HttpConnector;
import com.clientserver.app.connection.tcp.SocketNetworkClient;
import com.clientserver.app.connection.tcp.SocketNetworkServer;
import com.clientserver.app.connection.udp.UdpConnector;
import com.clientserver.app.graphobj.GraphObject;
import com.clientserver.app.graphobj.GraphObjectFactory;
import com.clientserver.app.graphobj.GraphObjectsType;
import com.clientserver.app.model.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private final Model model = new Model();
    private CallbackController callbackController;
    private boolean isConnected = false;
    private NetworkConnector networkConnector;
    @FXML
    private TextField host;
    @FXML
    private Spinner<Integer> port;
    @FXML
    private Pane root;
    @FXML
    private ChoiceBox<String> choiceGraphObject;
    @FXML
    private ChoiceBox<String> serverClientChoice;
    @FXML
    private ChoiceBox<String> protocolChoice;
    @FXML
    private Circle connectionStatus;
    @FXML
    private Spinner<Integer> sendObjectSpinner;
    @FXML
    private Spinner<Integer> requestObjectSpinner;
    @FXML
    private Label requestSizeLabel;

    @FXML
    public void initialize() {
        port.getValueFactory().setValue(9090);
        callbackController = new CallbackControllerJavaFxImp(this, model);
        ArrayList<String> choices = new ArrayList<>();
        for (GraphObjectsType type : GraphObjectsType.values())
            choices.add(type.toString());

        ObservableList<String> list = FXCollections.observableArrayList(choices);
        choiceGraphObject.setItems(list);
        choiceGraphObject.setValue(GraphObjectsType.CIRCLE.toString());

        serverClientChoice.setItems(FXCollections.observableArrayList("Server", "Client"));
        serverClientChoice.setValue("Client");
        serverClientChoice.getSelectionModel().selectedIndexProperty()
                .addListener((observableValue, number, number2) -> setConnectionStatus(false));

        protocolChoice.setItems(FXCollections.observableArrayList("TCP", "UDP", "HTTP"));
        protocolChoice.setValue("TCP");
        protocolChoice.getSelectionModel().selectedIndexProperty()
                .addListener((observableValue, number, number2) -> {
                    System.out.println(protocolChoice.getItems().get((Integer) number2));
                    setConnectionStatus(false);
                });

    }

    public void setConnectionStatus(boolean status) {
        isConnected = status;
        connectionStatus.setFill(status ? Color.GREEN : Color.RED);
    }

    private void connect() {
        boolean isServer = serverClientChoice.getValue().equals("Server");
        String protocol = protocolChoice.getValue();
        try {
            switch (protocol) {
                case "TCP" ->
                        networkConnector = isServer ? new SocketNetworkServer(port.getValue(), callbackController) :
                                new SocketNetworkClient(port.getValue(), callbackController);
                case "UDP" -> {
                    int listenPort = isServer ? port.getValue() : port.getValue() + 1;
                    int sendPort = isServer ? port.getValue() + 1 : port.getValue();
                    networkConnector = new UdpConnector(host.getText(), sendPort, listenPort, callbackController);
                }
                case "HTTP" -> networkConnector = new HttpConnector("http://localhost:9090", callbackController);
            }
            new Thread(networkConnector).start();
        } catch (IOException e) {
            setConnectionStatus(false);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error creating connection\n" + e.getMessage(), ButtonType.OK);
            alert.show();
        }
    }

    @FXML
    protected void onConnectButtonClick() {
        connect();
    }

    @FXML
    protected void onHelloButtonClick() {
        System.out.println("hello btn");
    }

    @FXML
    protected void onRemoveAllBtnClick() {
        if (isConnected) {
            networkConnector.removeAll();
        } else {
            System.out.println("No active connection");
        }
    }

    @FXML
    protected void onSendObjectBtnClick() {
        if (isConnected) {
            int id = sendObjectSpinner.getValue();
            if (id >= model.getGraphObjects().size()) {
                Alert alert = new Alert(Alert.AlertType.WARNING,
                        "Plane contains only %d elements but %d was requested".formatted(model.getGraphObjects().size(), id),
                        ButtonType.OK);
                alert.show();
            }
            List<GraphObject> list = new ArrayList<>();
            list.add(model.getGraphObjects().get(id));
            System.out.println("send + " + model.toJsonList(list));
            networkConnector.sendObject(model.toJsonList(list));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No active connection", ButtonType.OK);
            alert.show();
        }
    }

    @FXML
    protected void onSendObjectListBtnClick() {
        if (isConnected) {
            networkConnector.sendObjectList(model.toJsonList(model.getGraphObjects()));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No active connection", ButtonType.OK);
            alert.show();
        }
    }

    @FXML
    protected void onSendSizeBtnClick() {
        if (isConnected) {
            networkConnector.sendSize(model.getGraphObjects().size());
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No active connection", ButtonType.OK);
            alert.show();
        }
    }

    @FXML
    protected void onRequestObjectBtnClick() {
        if (isConnected) {
            networkConnector.requestObject(requestObjectSpinner.getValue());
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No active connection", ButtonType.OK);
            alert.show();
        }
    }

    @FXML
    protected void onRequestObjectListBtnClick() {
        if (isConnected) {
            networkConnector.requestObjectList();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No active connection", ButtonType.OK);
            alert.show();
        }
    }

    @FXML
    protected void onRequestSizeBtnClick() {
        if (isConnected) {
            networkConnector.requestSize();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No active connection", ButtonType.OK);
            alert.show();
        }
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
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.show();
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
                for (GraphObject go : model.getGraphObjects())
                    root.getChildren().add(go.draw());

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.show();
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
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.show();
        }
    }

    public void removeAll() {
        root.getChildren().clear();
    }

    public void addNode(Node object) {
        root.getChildren().add(object);
    }

    public void setRequestSize(int i) {
        requestSizeLabel.setText(String.valueOf(i));
    }
}