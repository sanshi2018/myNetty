package chapter13.client;

import chapter13.LogEvent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

public class LogEventDecoder extends MessageToMessageDecoder<DatagramPacket> {
    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket datagramPacket, List<Object> out) throws Exception {
        // 获取对DatagramPacket中的数据（ByteBuf）的引用
        ByteBuf data = datagramPacket.content();
        // 获取SEPARATOR的索引
        int idx = data.indexOf(0, data.readableBytes(), LogEvent.SEPARATOR);
        // 提取文件名
        String filename = data.slice(0, idx).toString(CharsetUtil.UTF_8);
        String logMsg = data.slice(idx + 1, data.readableBytes()).toString(CharsetUtil.UTF_8);
        // 构建一个新的LogEvent对象，并且将它添加到（已经解码的消息的）列表中
        LogEvent event = new LogEvent(datagramPacket.sender(), System.currentTimeMillis(), filename, logMsg);
        out.add(event);
    }
}
