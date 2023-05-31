package com.clientserver.app.connection.udp;

import com.clientserver.app.connection.CallbackController;
import com.clientserver.app.connection.Command;
import com.clientserver.app.connection.NetworkConnector;

import java.io.IOException;
import java.net.*;

public class UdpConnector implements NetworkConnector {
    private final DatagramSocket socket;
    private final InetAddress address;
    private final CallbackController callbackController;
    private final int sendPort;

    public UdpConnector(String address, int sendPort, int listenPort, CallbackController callbackController) throws SocketException, UnknownHostException {
        this.address = InetAddress.getByName(address);
        this.callbackController = callbackController;
        this.sendPort = sendPort;
        socket = new DatagramSocket(listenPort);
    }


    public void sendEcho(String msg) throws IOException {
        byte[] buf = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, sendPort);
        socket.send(packet);
    }

    @Override
    public void removeAll() {
        try {
            sendEcho(Command.REMOVE_TOKEN.toString());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void sendObject(String object) {
        try {
            sendEcho(Command.SEND_OBJECT + " " + object);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void sendObjectList(String objectList) {
        try {
            sendEcho(Command.SEND_OBJECT_LIST + " " + objectList);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void sendSize(int size) {
        try {
            sendEcho(Command.SEND_SIZE + " " + size);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void requestObject(int id) {
        try {
            sendEcho(Command.REQUEST_OBJECT + " " + id);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void requestObjectList() {
        try {
            sendEcho(Command.REQUEST_OBJECT_LIST.toString());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void requestSize() {
        try {
            sendEcho(Command.REQUEST_SIZE.toString());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void run() {
        System.out.println("Build new listener");
        callbackController.setConnectionStatus(true);

        byte[] buf = new byte[1024];
        while (!socket.isClosed()) {
            DatagramPacket packet
                    = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String[] received = new String(packet.getData(), 0, packet.getLength()).split(" ");
            switch (Command.valueOf(received[0])) {
                case REMOVE_TOKEN -> callbackController.deleteAll();
                case SEND_OBJECT -> callbackController.addObject(received[1]);
                case SEND_OBJECT_LIST -> callbackController.addObjectList(received[1]);
                case SEND_SIZE -> callbackController.setSize(Integer.parseInt(received[1]));
                case REQUEST_SIZE -> sendSize(callbackController.getSize());
                case REQUEST_OBJECT -> sendObject(callbackController.getObjectJson(received[1]));
                case REQUEST_OBJECT_LIST -> sendObjectList(callbackController.getOjectListJson());
            }
        }

    }
}
