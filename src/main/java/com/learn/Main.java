package com.learn;

import java.io.Closeable;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        ThreadLocal threadLocal = new ThreadLocal();
        Resource resource = new Resource();
        try (resource) {
        }
    }

    public static class Resource implements Closeable {

        @Override
        public void close() throws IOException {

        }
    }
}
