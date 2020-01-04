package com.justfors.client;

import com.justfors.stream.InputStream;
import com.justfors.stream.OutputStream;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Client extends Thread {

    private String host;
    private int port;
    private NetConnectionClient netConnectionClient;
    public static List<ClientConnection> connections = new CopyOnWriteArrayList<>();

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
                ClientConnection connection = new ClientConnection(socket, in, out, netConnectionClient);
                connections.add(connection);
                netConnectionClient.clientConnectionExecute(in, out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class ClientConnection {

        private Socket socket;
        private NetConnectionClient netConnectionClient;
        private InputStream in;
        private OutputStream out;

        ClientConnection(Socket socket, InputStream inputStream, OutputStream outputStream, NetConnectionClient netConnectionClient) throws IOException {
            this.socket = socket;
            this.netConnectionClient = netConnectionClient;
            in = inputStream;
            out = outputStream;
        }

        public Socket getSocket() {
            return socket;
        }

        public InputStream getIn() {
            return in;
        }

        public OutputStream getOut() {
            return out;
        }
    }
}
