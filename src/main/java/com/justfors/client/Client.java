package com.justfors.client;

import com.justfors.common.Connection;
import com.justfors.stream.InputStream;
import com.justfors.stream.OutputStream;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Client extends Thread {

    private String host;
    private int port;
    private NetConnectionClient netConnectionClient;
    public static List<ClientConnection> connections = new CopyOnWriteArrayList<>();
    private byte[] bufReceive = new byte[512];
    private byte[] bufSend = new byte[512];
    private boolean isTCP;
    private String connectionName;

    public Client(String host, int port, NetConnectionClient netConnectionClient, boolean isTCP) {
        this.host = host;
        this.port = port;
        this.netConnectionClient = netConnectionClient;
        this.isTCP = isTCP;
    }

    public void setBuf(byte[] buf) {
        this.bufReceive = buf;
        this.bufSend = buf;
    }

    public String getConnectionName() {
        return connectionName;
    }

    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public void run() {
        if (isTCP) {
            try (Socket socket = new Socket(host, port)) {
                try (InputStream in = new InputStream(new InputStreamReader(socket.getInputStream()));
                     OutputStream out = new OutputStream(new OutputStreamWriter(socket.getOutputStream()))) {
                    ClientConnection connection = new ClientConnection(socket, in, out, netConnectionClient);
                    connections.add(connection);
                    netConnectionClient.clientConnectionExecute(in, out, socket);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            try {
                DatagramSocket socket = new DatagramSocket();
                DatagramPacket packetReceive = new DatagramPacket(bufReceive, bufReceive.length, InetAddress.getByName(host), port);
                DatagramPacket packetSend = new DatagramPacket(bufSend, bufSend.length, InetAddress.getByName(host), port);

                new Thread(() -> {
                    try {
                        packetSend.setData(getConnectionName().getBytes());
                        socket.send(packetSend);
                        while (true) {
                            netConnectionClient.clientSend(socket, packetSend);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();

                new Thread(() -> {
                    while (true) {
                        try {
                            netConnectionClient.clientReceive(socket, packetReceive);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }).start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class ClientConnection extends Connection {

        private NetConnectionClient netConnectionClient;

        ClientConnection(Socket socket, InputStream inputStream, OutputStream outputStream, NetConnectionClient netConnectionClient) throws IOException {
            super(socket);
            this.netConnectionClient = netConnectionClient;
            setIn(inputStream);
            setOut(outputStream);
        }
    }

//    try {
//        DatagramSocket socket = new DatagramSocket();
//        DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(host), port);
//
//        new Thread(() -> {
//            while (true) {
//                try {
//                    netConnectionClient.clientReceive(socket, packet);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }).start();
//
//        new Thread(() -> {
//            try {
//                while (true) {
//                    netConnectionClient.clientSend(socket, packet);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).start();
//
//    } catch (IOException e) {
//        e.printStackTrace();
//    }







//    new Thread(() -> {
//        try {
//            DatagramSocket socket = new DatagramSocket(port+1);
//            DatagramPacket packet = new DatagramPacket(buf, buf.length);
//            clientPort = socket.getPort();
//            System.out.println(clientPort);
//            connection.set(true);
//            while (true) {
//                netConnectionClient.clientReceive(socket, packet);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }).start();
//
//            while (true) {
//        if (connection.get()) {
//            connection.set(false);
//            new Thread(() -> {
//                try {
//                    DatagramSocket socket = new DatagramSocket();
//                    DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(host), port);
//                    packet.setData(String.valueOf(clientPort).getBytes());
//                    socket.send(packet);
//                    while (true) {
//                        netConnectionClient.clientSend(socket, packet);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }).start();
//        }
//    }
}
