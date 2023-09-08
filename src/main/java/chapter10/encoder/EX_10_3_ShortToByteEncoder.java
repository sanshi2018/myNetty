package chapter10.encoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class EX_10_3_ShortToByteEncoder extends MessageToMessageEncoder<Short> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Short msg, List<Object> out) throws Exception {
        out.add(msg); // 将 Short 写入到编码消息的 List 中
    }
}
