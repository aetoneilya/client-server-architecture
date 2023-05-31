package com.clientserver.app.connection.tcp;

import com.clientserver.app.connection.CallbackController;

import java.io.IOException;
import java.net.Socket;

public class SocketNetworkClient extends SocketNetworkConnection {
    public SocketNetworkClient(int port, CallbackController callbackController) throws IOException {
        System.out.println("Build new client");
        socket = new Socket("localhost", port);
        super.init(socket, callbackController);
    }

}
