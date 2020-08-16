package com.justfors.client;

import com.justfors.stream.InputStream;
import com.justfors.stream.OutputStream;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

public interface NetConnectionClient {
    void clientConnectionExecute(InputStream in, OutputStream out, Socket socket) throws IOException;

    void clientReceive(DatagramSocket socket, DatagramPacket packet) throws IOException;

    void clientSend(DatagramSocket socket, DatagramPacket packet) throws IOException;
}
