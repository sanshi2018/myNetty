package chapter9.exception;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

public class FrameChunkDecoder extends ByteToMessageDecoder {
    private final int maxFrameSize;

    public FrameChunkDecoder(int maxFrameSize) {
        this.maxFrameSize = maxFrameSize;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int readableBytes = in.readableBytes(); // 检查是否有足够的字节用于读取下一帧
        if (readableBytes > maxFrameSize) { // 如果该帧太大，则丢弃它并抛出一个 TooLongFrameException……
            in.clear();
            throw new TooLongFrameException();
        }
        ByteBuf buf = in.readBytes(readableBytes); // 否则，从 ByteBuf 中读取一个新的帧
        out.add(buf); // 将该帧添加到解码消息的 List 中
    }
}
