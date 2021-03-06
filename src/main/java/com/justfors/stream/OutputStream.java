package com.justfors.stream;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

public class OutputStream extends BufferedWriter {

    public OutputStream(Writer out) {
        super(out);
    }

    public void send(String msg) {
        try {
            write(msg + "\n");
            flush();
        } catch (IOException ignored) {}
    }

    public void send(byte[] bytes) {
        try {
            write(Arrays.toString(bytes).replaceAll("\\[","").replaceAll("]","").replaceAll("\\s", "") + "\n");
            flush();
        } catch (IOException ignored) {}
    }
}
