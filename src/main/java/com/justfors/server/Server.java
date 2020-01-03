package com.justfors.server;

import com.justfors.stream.InputStream;
import com.justfors.stream.OutputStream;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server extends Thread {

    private int port;
    private NetConnectionServer netConnectionServer;
    public static List<ServerConnection> connections = new ArrayList<>();

    public Server(int port, NetConnectionServer netConnectionServer){
        this.port = port;
        this.netConnectionServer = netConnectionServer;
    }

    public int getPort() {
        return port;
    }

    @Override
    public void run() {
        try(ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("IP of my system is := " + getCurrentIP() + ":" + serverSocket.getLocalPort());
            while (true) {
                Socket socket = serverSocket.accept();
                ServerConnection connection = new ServerConnection(socket, netConnectionServer);
                connections.add(connection);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getCurrentIP() {
        String ipAddress = null;
        try {
            URLConnection connection = new URL("https://myip.by/").openConnection();
            try (Scanner scanner = new Scanner(connection.getInputStream())){
                scanner.useDelimiter("\\Z");
                String content = scanner.next();
                int indStart = content.indexOf("\">whois ");
                int indEnd = content.indexOf("</a>", indStart);

                ipAddress = content.substring(indStart + 8, indEnd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ipAddress;
    }

    public class ServerConnection extends Thread {

        private Socket socket;
        private NetConnectionServer netConnectionServer;
        private InputStream in;
        private OutputStream out;

        ServerConnection(Socket socket, NetConnectionServer netConnectionServer) throws IOException {
            this.socket = socket;
            this.netConnectionServer = netConnectionServer;
            in = new InputStream(new InputStreamReader(socket.getInputStream()));
            out = new OutputStream(new OutputStreamWriter(socket.getOutputStream()));
            start();
        }

        public void run() {
            // Here must be your logic for connection
            try {
                netConnectionServer.serverConnectionExecute(in, out, socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //****************************************
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

