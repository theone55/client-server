package com.justfors.stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class InputStream extends BufferedReader {

    public InputStream(Reader in) {
        super(in);
    }

    public byte[] readBytes() throws IOException {
        return convertStringToByte(this.readLine());
    }

    private byte[] convertStringToByte(String stringBytes){
        String[] pseudoBytes = stringBytes.split(",");
        byte[] data = new byte[pseudoBytes.length];
        for (int i = 0; i < pseudoBytes.length; i++) {
            int byteBeforeConvert = Integer.parseInt(pseudoBytes[i]);
            data[i] = (byte)byteBeforeConvert;
        }
        return data;
    }

}
