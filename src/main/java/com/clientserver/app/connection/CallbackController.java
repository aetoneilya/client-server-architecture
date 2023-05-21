package com.clientserver.app.connection;

import com.clientserver.app.graphobj.GraphObject;

import java.util.List;

public interface CallbackController {
    void deleteAll();

    void addObject(String go);

    void addObjectList(String graphObjectList);

    String getObjectJson(String index);

    int getSize();

    void setConnectionStatus(boolean status);
}
