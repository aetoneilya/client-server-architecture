package com.clientserver.app.connection.tcp;

import com.clientserver.app.connection.CallbackController;
import com.clientserver.app.connection.Command;
import com.clientserver.app.connection.NetworkConnector;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public abstract class SocketNetworkConnection implements NetworkConnector {
    protected PrintWriter writer;
    protected Scanner reader;
    protected Socket socket;
    protected CallbackController callbackController;

    public void init(Socket socket, CallbackController callbackController) throws IOException {
        try {
            this.socket = socket;
            reader = new Scanner(socket.getInputStream());
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.callbackController = callbackController;
    }

    @Override
    public void run() {
        System.out.println("Build new listener");
        callbackController.setConnectionStatus(true);
        while (!socket.isClosed()) {
            System.out.println("listen loop");
            String input = "";
            if (reader.hasNextLine()) {
                input = reader.nextLine();
            }

            switch (Command.valueOf(input)) {
                case REMOVE_TOKEN -> callbackController.deleteAll();
                case SEND_OBJECT -> callbackController.addObject(reader.nextLine());
                case SEND_OBJECT_LIST -> callbackController.addObjectList(reader.nextLine());
                case SEND_SIZE -> System.out.println("size: " + reader.nextLine());
                case REQUEST_SIZE -> sendSize(callbackController.getSize());
                case REQUEST_OBJECT -> sendObject(callbackController.getObjectJson(reader.nextLine()));
            }

            System.out.println("From user: " + input);
        }
    }

    @Override
    public void removeAll() {
        writer.println(Command.REMOVE_TOKEN);
    }

    @Override
    public void sendObject(String object) {
        System.out.println("send: " + object);
        writer.println(Command.SEND_OBJECT);
        writer.println(object);
    }

    @Override
    public void sendObjectList(String objectList) {
        System.out.println("send list: " + objectList);
        writer.println(Command.SEND_OBJECT_LIST);
        writer.println(objectList);
    }

    @Override
    public void sendSize(int size) {
        System.out.println("send size: " + size);
        writer.println(Command.SEND_SIZE);
        writer.println(size);
    }

    @Override
    public void requestObject(int id) {
        System.out.println("request id: " + id);
        writer.println(Command.REQUEST_OBJECT);
        writer.println(id);
    }

    @Override
    public void requestSize() {
        System.out.println("send size");
        writer.println(Command.REQUEST_SIZE);
    }
}
