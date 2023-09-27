package chapter13.server;

import chapter13.LogEvent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.java.Log;

import java.net.InetSocketAddress;
import java.util.List;

public class LogEventEncoder extends MessageToMessageEncoder<LogEvent> {
    private final InetSocketAddress remoteAddress;
    /**
      *  LogEventEncoder创建了即将被发送到指定的InetSocketAddress的DatagramPacket消息
     **/
    public LogEventEncoder(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }
    @Override
    protected void encode(ChannelHandlerContext ctx, LogEvent logEvent, List<Object> out) throws Exception {
        byte[] file = logEvent.getLogfile().getBytes(CharsetUtil.UTF_8);
        byte[] msg = logEvent.getMsg().getBytes(CharsetUtil.UTF_8);
        ByteBuf buf = ctx.alloc().buffer(file.length + msg.length + 1);
        // 将文件名写入到ByteBuf中
        buf.writeBytes(file);
        // 添加一个SEPARATOR
        buf.writeByte(LogEvent.SEPARATOR);
        // 将日志消息写入ByteBuf中
        buf.writeBytes(msg);
        // 将一个拥有数据和目的地地址的新DatagramPacket添加到出站的消息列表中
        out.add(new DatagramPacket(buf, remoteAddress));
    }
}
