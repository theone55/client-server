package com.justfors.client;

import com.justfors.stream.InputStream;
import com.justfors.stream.OutputStream;

import java.io.IOException;

public interface NetConnectionClient {
    void clientConnectionExecute(InputStream in, OutputStream out) throws IOException;
}
