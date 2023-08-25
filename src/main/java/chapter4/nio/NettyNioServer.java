package chapter4.nio;

import chapter4.bio.NettyOioServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;
import io.netty.util.CharsetUtil;

public class NettyNioServer {
    public static void main(String[] args) {
        new NettyNioServer().server(8080);
    }
    public void server(int port) {
        final ByteBuf buf = Unpooled.unreleasableBuffer(
                Unpooled.copiedBuffer("Hi!\r\n", CharsetUtil.UTF_8));
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class) // 使用 OioEventLoopGroup 以允许阻塞模式（旧的 I/O）
                    .localAddress(port)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            // 添加一个 ChannelInboundHandlerAdapter 以拦截和处理事件
                            ch.pipeline().addLast(
                                    new io.netty.channel.ChannelInboundHandlerAdapter() {
                                        @Override
                                        public void channelActive(
                                                io.netty.channel.ChannelHandlerContext ctx) throws Exception {
                                            // 将消息写到客户端，并添加 ChannelFutureListener，以便消息一被写完就关闭连接
                                            ctx.writeAndFlush(buf.duplicate())
                                                    .addListener(io.netty.channel.ChannelFutureListener.CLOSE);

//                                            ctx.channel().
                                        }
                                    });
                        }
                    });
            ChannelFuture future = b.bind().sync();
            future.channel().closeFuture().sync();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
