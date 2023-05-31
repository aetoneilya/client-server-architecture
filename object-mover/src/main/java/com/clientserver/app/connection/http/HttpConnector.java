package com.clientserver.app.connection.http;

import com.clientserver.app.connection.CallbackController;
import com.clientserver.app.connection.NetworkConnector;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;


public class HttpConnector implements NetworkConnector {
    private final CallbackController callbackController;
    private final FiguresClient client;

    public HttpConnector(String url, CallbackController callbackController) {
        this.callbackController = callbackController;

        client = new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create()).build().create(FiguresClient.class);
    }

    @Override
    public void removeAll() {
        try {
            client.deleteFigures().execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendObject(String object) {
        try {
            System.out.println("send");
            System.out.println(object);
            client.postUploadFigure(object).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendObjectList(String objectList) {
        sendObject(objectList);
    }

    @Override
    public void sendSize(int size) {
    }

    @Override
    public void requestObject(int id) {
        try {
            String obj = client.getFigureById(id).execute().body();
            callbackController.addObject("[" + obj + "]");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void requestObjectList() {
        try {
            String obj = client.getFigures().execute().body();
            callbackController.addObjectList(obj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void requestSize() {
        try {
            Long count = client.getCountFigures().execute().body();
            callbackController.setSize(count == null ? 0 : count.intValue());
            System.out.println("size is " + count);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        callbackController.setConnectionStatus(true);
    }
}
