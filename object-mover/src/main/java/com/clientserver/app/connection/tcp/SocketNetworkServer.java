package com.clientserver.app.connection.tcp;

import com.clientserver.app.connection.CallbackController;

import java.io.IOException;
import java.net.ServerSocket;


public class SocketNetworkServer extends SocketNetworkConnection {
    private final ServerSocket serverSocket;
    private final CallbackController callbackController;

    public SocketNetworkServer(int port, CallbackController callbackController) throws IOException {
        System.out.println("build new server");
        serverSocket = new ServerSocket(port);
        this.callbackController = callbackController;
    }

    @Override
    public void run() {
        System.out.println("waiting for connection");
        try {
            super.init(serverSocket.accept(), callbackController);
            System.out.println("new connection");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        super.run();
    }
}
