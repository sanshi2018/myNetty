package chapter4.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class PlaninNioServer {
    public static void main(String[] args) {
        PlaninNioServer planinNioServer = new PlaninNioServer();
        try {
            planinNioServer.server(8080);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void server(int port) throws Exception {
        System.out.println("Listening for connections on port " + port);
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        InetSocketAddress address = new InetSocketAddress(port);
        // 将服务器绑定到选定的端口
        serverSocket.bind(address);
        // 打开 Selector 来处理 Channel
        Selector selector = Selector.open();
        // 将 ServerSocket 注册到 Selector 以接受连接
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        final ByteBuffer msg = ByteBuffer.wrap("Hi!\r\n".getBytes());

        for (;;) {
            try {
                selector.select();
            }catch (Exception e){
                e.printStackTrace();
                break;
            }

            // 获取所有接收事件的 SelectionKey 实例
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove(); // 删除当前的 SelectionKey，以防止重复处理
                try {
                    if (key.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        // 接受客户端，并将它注册到选择器
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ, msg.duplicate());
                        System.out.println("Accepted connection from " + client);

                    }
                    if (key.isWritable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();

                        while (buffer.hasRemaining()) {
                            // 将数据写到已连接的客户端
                            if (client.write(buffer) == 0) {
                                break;
                            }
                        }
                    }

                }catch (IOException e) {
                    key.cancel();
                    try {
                        key.channel().close();
                    }catch (IOException ex) {
                        // ignore on close
                    }
                }
            }
        }
    }
}
