package com.justfors.server;

import com.justfors.stream.InputStream;
import com.justfors.stream.OutputStream;

import java.io.IOException;
import java.net.Socket;

public interface NetConnectionServer {
    void serverConnectionExecute(InputStream in, OutputStream out, Socket socket) throws IOException;
}
