package com.justfors.common;

import com.justfors.server.NetConnectionServer;
import com.justfors.stream.InputStream;
import com.justfors.stream.OutputStream;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Connection {

    private Socket socket;
    private InputStream in;
    private OutputStream out;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        in = new InputStream(new InputStreamReader(socket.getInputStream()));
        out = new OutputStream(new OutputStreamWriter(socket.getOutputStream()));
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

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setIn(InputStream in) {
        this.in = in;
    }

    public void setOut(OutputStream out) {
        this.out = out;
    }
}
