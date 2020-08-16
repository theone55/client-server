package com.justfors.server;

import com.justfors.stream.InputStream;
import com.justfors.stream.OutputStream;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

public interface NetConnectionServer {
    void serverConnectionExecute(InputStream in, OutputStream out, Socket socket) throws IOException;

    void serverReceive(DatagramSocket socket, DatagramPacket packet) throws IOException;

    void serverSend(DatagramSocket socket, DatagramPacket packet) throws IOException;

}
