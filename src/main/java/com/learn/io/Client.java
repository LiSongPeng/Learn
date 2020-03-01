package com.learn.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        if (socketChannel.connect(new InetSocketAddress("localhost", 8888))) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(48);
            byteBuffer.clear();
            byteBuffer.put("hello world".getBytes());
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
        }
    }
}
