import com.justfors.client.Client;
import com.justfors.client.NetConnectionClient;
import com.justfors.stream.InputStream;
import com.justfors.stream.OutputStream;

import java.io.IOException;
import java.net.Socket;

public class ClientTest implements NetConnectionClient {
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

    public static void main(String[] args) {
        new Client("localhost", 4444, new ClientTest()).start();
        new Client("localhost", 4444, new ClientTest()).start();
        new Client("localhost", 4444, new ClientTest()).start();
        new Client("localhost", 5555, new ClientTest()).start();
        new Client("localhost", 7777, new ClientTest()).start();
    }
}
