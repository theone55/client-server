import com.justfors.client.Client;
import com.justfors.client.NetConnectionClient;
import com.justfors.stream.InputStream;
import com.justfors.stream.OutputStream;

import java.io.IOException;

public class ClientTest implements NetConnectionClient {
    @Override
    public void clientConnectionExecute(InputStream in, OutputStream out) throws IOException {
        while (true) out.send("qwe " + out.toString());
    }

    public static void main(String[] args) {
        new Client("93.125.42.194", 4444, new ClientTest()).start();
        new Client("93.125.42.194", 4444, new ClientTest()).start();
        new Client("93.125.42.194", 4444, new ClientTest()).start();
    }
}
