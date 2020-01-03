import com.justfors.server.NetConnectionServer;
import com.justfors.server.Server;
import com.justfors.stream.InputStream;
import com.justfors.stream.OutputStream;

import java.io.IOException;
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

    public static void main(String[] args) {
        new Server(4444, new ServerTest()).start();
    }
}
