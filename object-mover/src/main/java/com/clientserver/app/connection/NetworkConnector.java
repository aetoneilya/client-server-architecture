package com.clientserver.app.connection;

public interface NetworkConnector extends Runnable {
    void removeAll();

    void sendObject(String object);

    void sendObjectList(String objectList);

    void sendSize(int size);

    void requestObject(int id);

    void requestObjectList();

    void requestSize();
}
