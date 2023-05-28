package com.clientserver.app.connection;

public interface CallbackController {
    void deleteAll();

    void addObject(String go);

    void addObjectList(String graphObjectList);

    String getObjectJson(String index);

    String getOjectListJson();

    int getSize();

    void setConnectionStatus(boolean status);

    void setSize(int i);
}
