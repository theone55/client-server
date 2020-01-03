package com.justfors.client;

import com.justfors.stream.InputStream;
import com.justfors.stream.OutputStream;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client extends Thread {

    private String host;
    private int port;
    private NetConnectionClient netConnectionClient;

    public Client(String host, int port, NetConnectionClient netConnectionClient) {
        this.host = host;
        this.port = port;
        this.netConnectionClient = netConnectionClient;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public void run() {
        try (Socket socket = new Socket(host, port)) {
            try (InputStream in = new InputStream(new InputStreamReader(socket.getInputStream()));
                 OutputStream out = new OutputStream(new OutputStreamWriter(socket.getOutputStream()))) {
                netConnectionClient.clientConnectionExecute(in, out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
