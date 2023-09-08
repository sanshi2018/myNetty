package chapter9.inboundMsg;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class FixedLengthFrameDecoder extends ByteToMessageDecoder {
    private final int frameLength;

    // 指定要生成的帧的长度
    public FixedLengthFrameDecoder(int frameLength) {
        if (frameLength <= 0) {
            throw new IllegalArgumentException("frameLength must be a positive integer: " + frameLength);
        }

        this.frameLength = frameLength;
    }
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < frameLength) {
            System.out.println("-----------------");
        }
        while (in.readableBytes() >= frameLength) { // 检查是否有足够的字节用于读取一个完整的帧
            ByteBuf buf = in.readBytes(frameLength); // 从 ByteBuf 中读取一个新帧
            out.add(buf); // 将该帧添加到已被解码的消息列表中
        }
    }
}
