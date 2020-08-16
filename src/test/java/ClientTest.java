import com.justfors.client.Client;
import com.justfors.client.NetConnectionClient;
import com.justfors.stream.InputStream;
import com.justfors.stream.OutputStream;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.Scanner;

public class ClientTest implements NetConnectionClient {

    private static volatile String msg = null;
    @Override
    public void clientConnectionExecute(InputStream in, OutputStream out, Socket socket) throws IOException {
        while (true) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (Client.ClientConnection connection : Client.connections) {
                OutputStream outputStream = connection.getOut();
                outputStream.send("qwe " + out.toString());
            }

        }
    }

    @Override
    public void clientReceive(DatagramSocket socket, DatagramPacket packet) throws IOException {
        if (msg != null) {
            packet.setData(msg.getBytes());
            socket.send(packet);
            socket.receive(packet);
            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println(packet.getLength());
            System.out.println(packet.getData());
            System.out.println(received);
            msg = null;
        }
    }

    @Override
    public void clientSend(DatagramSocket socket, DatagramPacket packet) throws IOException {

    }

    public static void main(String[] args) {
        new Client("localhost", 4444, new ClientTest(), false).start();

        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (true) {
                msg = sc.nextLine();
            }
        }).start();
//        new Client("localhost", 4444, new ClientTest()).start();
//        new Client("localhost", 4444, new ClientTest()).start();
//        new Client("localhost", 5555, new ClientTest()).start();
//        new Client("localhost", 7777, new ClientTest()).start();
    }
}

