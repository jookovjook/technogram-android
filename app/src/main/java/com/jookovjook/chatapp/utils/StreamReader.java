package com.jookovjook.chatapp.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamReader {

    public static String read(InputStream inputStream){
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = inputStream.read();
            while(i != -1) {
                bo.write(i);
                i = inputStream.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }
}
