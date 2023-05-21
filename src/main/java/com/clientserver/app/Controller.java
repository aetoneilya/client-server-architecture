package com.clientserver.app;

import com.clientserver.app.connection.CallbackController;
import com.clientserver.app.connection.CallbackControllerJavaFxImp;
import com.clientserver.app.connection.NetworkConnector;
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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Controller {
    private final Model model = new Model();
    private CallbackController callbackController;
    private boolean isConnected = false;
    private NetworkConnector networkConnector;
    @FXML
    private Button connectBtn;
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
    public void initialize() {

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

        protocolChoice.setItems(FXCollections.observableArrayList("TCP", "UDP"));
        protocolChoice.setValue("TCP");
        protocolChoice.getSelectionModel().selectedIndexProperty()
                .addListener((observableValue, number, number2) ->
                        System.out.println(protocolChoice.getItems().get((Integer) number2)));

    }

    public void setConnectionStatus(boolean status) {
        isConnected = status;
        connectionStatus.setFill(status ? Color.GREEN : Color.RED);
    }

    private void connect() {
        boolean isServer = serverClientChoice.getValue().equals("Server");
        String protocol = protocolChoice.getValue();
        try {
            if (protocol.equals("TCP")) {
                networkConnector = isServer ? new SocketNetworkServer(port.getValue(), callbackController) :
                        new SocketNetworkClient(port.getValue(), callbackController);
            } else {
                int listenPort = isServer ? port.getValue() : port.getValue() + 1;
                int sendPort = isServer ? port.getValue() + 1 : port.getValue();
                networkConnector = new UdpConnector("localhost", sendPort, listenPort, callbackController);
            }
            new Thread(networkConnector).start();
        } catch (IOException e) {
            setConnectionStatus(false);
            System.out.println("error creating connection : " + e.getMessage());
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
        //DONT WORK!
//        if (isConnected) {
//            int id = sendObjectSpinner.getValue();
//            if (id < model.getGraphObjects().size()) return;
//            List<GraphObject> list = List.of(model.getGraphObjects().get(id));
//            networkConnector.sendObject(model.toJsonList(list));
//        } else {
//            System.out.println("No active connection");
//        }
    }

    @FXML
    protected void onSendObjectListBtnClick() {
        if (isConnected) {
            networkConnector.sendObjectList(model.toJsonList(model.getGraphObjects()));
        } else {
            System.out.println("No active connection");
        }
    }

    @FXML
    protected void onSendSizeBtnClick() {
        if (isConnected) {
            networkConnector.sendSize(model.getGraphObjects().size());
        } else {
            System.out.println("No active connection");
        }
    }

    @FXML
    protected void onRequestObjectBtnClick() {
        //DONT WORK!
//        if (isConnected) {
//            networkConnector.requestObject(requestObjectSpinner.getValue());
//        } else {
//            System.out.println("No active connection");
//        }
    }

    @FXML
    protected void onRequestSizeBtnClick() {
        if (isConnected) {
            networkConnector.requestSize();
        } else {
            System.out.println("No active connection");
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
                for (GraphObject go : model.getGraphObjects())
                    root.getChildren().add(go.draw());

            } catch (Exception e) {
                System.out.println(e.getMessage());
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

    public void removeAll() {
        root.getChildren().clear();
    }

    public void addNode(Node object) {
        root.getChildren().add(object);
    }
}