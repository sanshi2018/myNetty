package chapter11.FileRegion;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.stream.ChunkedStream;

import java.io.File;
import java.io.FileInputStream;

public class WriteStreamHandler extends ChannelInboundHandlerAdapter {
    private final File file;
    public WriteStreamHandler(File file) {
        this.file = file;
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ctx.writeAndFlush(new ChunkedStream(new FileInputStream("file")));
    }
}
