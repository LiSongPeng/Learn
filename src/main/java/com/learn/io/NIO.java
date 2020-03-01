package com.learn.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class NIO {

    public static void main(String[] args) {
        ServerSocketChannelThread serverSocketChannelThread = new ServerSocketChannelThread();
        serverSocketChannelThread.startRunning();
        Scanner scanner = new Scanner(System.in);
        System.out.println("please input stop to stop!");
        if (scanner.next() == "stop") {
            serverSocketChannelThread.stopRunning();
        }
    }
}

class ServerSocketChannelThread extends Thread {
    private static final int PORT = 8888;
    private volatile boolean running = false;

    @Override
    public void run() {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress("localhost", PORT));
            Selector selector = Selector.open();
            List<SocketChannel> socketChannelList = new LinkedList<>();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (running) {
                selector.select(selectionKey -> {
                    if (selectionKey.isAcceptable()) {
                        try {
                            SocketChannel socketChannel = ((ServerSocketChannel) selectionKey.channel()).accept();
                            if (socketChannel != null) {
                                System.out.println("accepted");
                                socketChannel.configureBlocking(false);
                                socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (selectionKey.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
                        byteBuffer.clear();
                        byteBuffer.flip();
                        try {
                            int readied = socketChannel.read(byteBuffer);
                            if (readied == -1) {
                                socketChannel.close();
                            }
                            if (readied > 0) {
                                System.out.println("received:" + new String(byteBuffer.array(), 0, byteBuffer.limit()));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            for (SocketChannel socketChannel : socketChannelList) {
                socketChannel.close();
            }
            serverSocketChannel.close();
            selector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startRunning() {
        this.running = true;
        this.start();
    }

    public void stopRunning() {
        this.running = false;
    }
}
