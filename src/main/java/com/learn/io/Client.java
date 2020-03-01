package com.learn.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Client {
    public static void doWrite(SocketChannel socketChannel) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(11);
        byteBuffer.put("hello world".getBytes());
        byteBuffer.flip();
        try {
            socketChannel.write(byteBuffer);
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        boolean connected = socketChannel.connect(new InetSocketAddress("localhost", 8888));
        if (!connected) {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        } else {
            doWrite(socketChannel);
            selector.close();
            return;
        }
        while (true) {
            if (selector.select() == 0) {
                continue;
            }
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                SocketChannel channel = (SocketChannel) selectionKey.channel();
                if (selectionKey.isConnectable()) {
                    System.out.println("can connect");
                    channel.finishConnect();
                    doWrite(socketChannel);
                    selector.close();
                    return;
                }
            }
        }
    }
}
