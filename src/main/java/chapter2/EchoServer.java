package chapter2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
//        if (args.length != 1) {
//            System.out.println(
//                "Usage: " + EchoServer.class.getSimpleName() +
//                " <port>");
//        }
        // 设置端口值(如果端口参数的格式不正确, 则抛出一个NumberFormatException)
        int port = Integer.parseInt("8080");
        // 调用服务器的 start() 方法
        new EchoServer(port).start();
    }

    public void start() throws InterruptedException {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        // 创建 EventLoopGroup
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 创建 ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
             .channel(NioServerSocketChannel.class) // 指定所使用的 NIO 传输 Channel
             .localAddress(new InetSocketAddress(port)) // 使用指定的端口设置套接字地址
             .childHandler(new ChannelInitializer<SocketChannel>() {// 添加一个 EchoServerHandler 到子 Channel 的 ChannelPipeline
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ch.pipeline().addLast(serverHandler); // EchoServerHandler 被标注为 @Shareable, 所以我们可以总是使用同样的实例
                 }
             });

            // 异步地绑定服务器; 调用 sync() 方法阻塞等待直到绑定完成
            // 对 sync() 的调用将导致当前 Thread 阻塞, 一直到绑定操作完成为止
            // 如果绑定失败, 则抛出异常
            ChannelFuture future = b.bind().sync();
            // 获取 Channel 的 CloseFuture, 并且阻塞当前线程直到它完成
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
