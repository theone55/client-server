package com.justfors.client;

import com.justfors.stream.InputStream;
import com.justfors.stream.OutputStream;

import java.io.IOException;
import java.net.Socket;

public interface NetConnectionClient {
    void clientConnectionExecute(InputStream in, OutputStream out, Socket socket) throws IOException;
}
