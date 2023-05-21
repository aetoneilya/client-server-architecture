package com.clientserver.app.connection;

import com.clientserver.app.graphobj.GraphObject;

import java.util.List;

public interface NetworkConnector extends Runnable {
    void removeAll();

    void sendObject(String object);

    void sendObjectList(String objectList);

    void sendSize(int size);

    void requestObject(int id);

    void requestSize();
}
