package com.justfors.server;

import com.justfors.common.Connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server extends Thread {

    private int port;
    private NetConnectionServer netConnectionServer;
    public static List<ServerConnection> connections = new CopyOnWriteArrayList<>();
    public static Map<String, String> udpConnections = Collections.synchronizedMap(new LinkedHashMap());
    private byte[] bufReceive = new byte[512];
    private byte[] bufSend = new byte[512];
    private boolean isTCP;

    private AtomicBoolean connected = new AtomicBoolean(false);

    public Server(int port, NetConnectionServer netConnectionServer, boolean isTCP) {
        this.port = port;
        this.netConnectionServer = netConnectionServer;
        this.isTCP = isTCP;
    }

    public void setBuf(byte[] buf) {
        this.bufReceive = buf;
        this.bufSend = buf;
    }

    public int getPort() {
        return port;
    }

    @Override
    public void run() {
        if (isTCP) {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                showCurrentConnection(serverSocket);
                while (true) {
                    Socket socket = serverSocket.accept();
                    ServerConnection connection = new ServerConnection(socket, netConnectionServer);
                    connections.add(connection);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                DatagramSocket socket = new DatagramSocket(port);
                showCurrentConnection(socket);
                DatagramPacket packetReceive = new DatagramPacket(bufReceive, bufReceive.length);

                new Thread(() -> {
                    try {
                        socket.receive(packetReceive);
                        String connectionName = new String(packetReceive.getData(), 0, packetReceive.getLength());
                        if (!udpConnections.containsKey(connectionName)) {
                            System.out.println("Connected : " + connectionName + "    " +  packetReceive.getAddress().getHostName() + ":" + packetReceive.getPort());
                            udpConnections.put(connectionName, packetReceive.getAddress().getHostName() + ":" + packetReceive.getPort());
                            connected.set(true);
                        }
                        while (true) {
                            netConnectionServer.serverReceive(socket, packetReceive);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();

                while (true) {
                    if (connected.get()) {
                        connected.set(false);
                        new Thread(() -> {
                            try {
                                String[] connectionData = ((String)udpConnections.values().toArray()[udpConnections.size() -1]).split(":");
                                String host = connectionData[0];
                                Integer port = Integer.valueOf(connectionData[1]);
                                DatagramPacket packetSend = new DatagramPacket(bufSend, bufSend.length, InetAddress.getByName(host), port);
                                while (true) {
                                    netConnectionServer.serverSend(socket, packetSend);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }).start();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void showCurrentConnection(DatagramSocket serverSocket) {
        showCurrentConnection(serverSocket.getLocalPort(), "UDP");
    }

    private static void showCurrentConnection(ServerSocket serverSocket) {
        showCurrentConnection(serverSocket.getLocalPort(), "TCP");
    }

    private static void showCurrentConnection(int port, String protocol) {
        System.out.println("IP of my system is := " + getCurrentIP() + ":" + port + " - " + protocol);
    }

    public static String getCurrentIP() {
        String ipAddress = null;
        try {
            URLConnection connection = new URL("https://myip.by/").openConnection();
            try (Scanner scanner = new Scanner(connection.getInputStream())) {
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

    public class ServerConnection extends Connection implements Runnable {

        private NetConnectionServer netConnectionServer;

        public ServerConnection(Socket socket, NetConnectionServer netConnectionServer) throws IOException {
            super(socket);
            this.netConnectionServer = netConnectionServer;
            new Thread(this).start();
        }

        public void run() {
            // Here must be your logic for connection
            try {
                netConnectionServer.serverConnectionExecute(getIn(), getOut(), getSocket());
            } catch (IOException e) {
                e.printStackTrace();
            }
            //****************************************
        }
    }
}

