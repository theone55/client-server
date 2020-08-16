import com.justfors.server.NetConnectionServer;
import com.justfors.server.Server;
import com.justfors.stream.InputStream;
import com.justfors.stream.OutputStream;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

public class ServerTest implements NetConnectionServer {
    @Override
    public void serverConnectionExecute(InputStream in, OutputStream out, Socket socket) throws IOException {
        System.out.println("New connection : " + socket);
        while (true) {
            try {
                String userMessage = in.readLine();
                if (userMessage != null && !userMessage.equals("")) {
                    System.out.println(userMessage);
                }
            } catch (SocketException e){
                System.out.println("Connection close..." + socket);
                break;
            }
        }
    }

    @Override
    public void serverReceive(DatagramSocket socket, DatagramPacket packet) throws IOException {
        socket.receive(packet);
        String received = new String(packet.getData(), 0, packet.getLength());
        System.out.println(received);
        String response = (received + "-!");
        packet.setData(response.getBytes(), 0, response.getBytes().length);
        System.out.println(packet.getLength());
        System.out.println(packet.getData());
        socket.send(packet);
    }

    @Override
    public void serverSend(DatagramSocket socket, DatagramPacket packet) throws IOException {

    }

    public static void main(String[] args) {
        new Server(4444, new ServerTest(), false).start();
//        new Server(7777, new ServerTest()).start();
//        new Server(5555, new ServerTest()).start();
    }
}
