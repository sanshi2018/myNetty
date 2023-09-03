package chapter8;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * 假设你的服务器正在处理一个客户端的请求，这个请求需要服务器充当第三方系统的客户端。
 * 当一个应用程序(如一个代理服务器)必须要和组织现有的系统(如 Web 服务或者数据库)集成时，
 * 就可能发生这种情况。在这种情况下，将需要从已经被接受的子 Channel 中启动一个引导客户端Channel。
 */
public class EX_8_4_reuseEventLopp {
    /**
     * 通过将已被接受的子Channel的EventLoop传递给 Bootstrap的group()方法来共享该 EventLoop。
     * 因为分配给 EventLoop 的所有 Channel 都使用同一 个线程，所以这避免了额外的线程创建
     */
    void func() {
        ServerBootstrap bootstrap = new ServerBootstrap();//  创建 ServerBootstrap 以创建 ServerSocketChannel，并绑定它
        ServerBootstrap channel = bootstrap
                .group(new NioEventLoopGroup(), new NioEventLoopGroup()) //  设置 EventLoopGroup，其将提供用 以处理 Channel 事件的 EventLoop
                .channel(NioServerSocketChannel.class) // 指定要使用的 Channel 实现
                .childHandler(
                        new SimpleChannelInboundHandler<ByteBuf>() {
                            ChannelFuture connectFuture;
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                // 创建一个 Bootstrap 类的实例以连接到远程主机
                                Bootstrap bootstrap = new Bootstrap();
                                // 指定 Channel 的实现
                                bootstrap.channel(NioSocketChannel.class)
                                        .handler(
                                                new SimpleChannelInboundHandler<ByteBuf>() {
                                                    @Override
                                                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                                                        System.out.println("Received data");
                                                    }
                                                }
                                        );
                                // 使用与分配给已被接受的子 Channel 相同的 EventLoop
                                bootstrap.group(ctx.channel().eventLoop());
                                // 连接到远程节点
                                connectFuture = bootstrap.connect(
                                        new InetSocketAddress("www.manning.com", 80));
                            }

                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                                if (connectFuture.isDone()) {
                                    // 当连接完成时，执行一些数据操作（如代理）
                                }
                            }
                        });
        ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080)); // 通过配置好的 ServerBootstrap 绑定该 ServerSocketChannel
        future.addListener((ChannelFuture f) -> {
            if (f.isSuccess()) {
                System.out.println("Server bound");
            } else {
                System.err.println("Bound attempt failed");
                f.cause().printStackTrace();
            }
        });
    }
}
