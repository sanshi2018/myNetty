package chapter11.delimited;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LineBasedFrameDecoder;

public final class CmdDecoder extends LineBasedFrameDecoder {
    final byte SPACE = (byte)' ';
    public CmdDecoder(int maxLength) {
        super(maxLength);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
        // 从ByteBuf中提取由行尾符序列分隔的帧
        ByteBuf frame = (ByteBuf)super.decode(ctx, buffer);
        if (frame == null) {
            return null; // 未找到帧，返回null
        }
        int index = frame.indexOf(frame.readerIndex(), frame.writerIndex(), SPACE);
        return new Cmd(frame.slice(frame.readerIndex(), index), frame.slice(index + 1, frame.writerIndex()));
    }
}
