package chapter11.delimited;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class CmdHandler extends SimpleChannelInboundHandler<Cmd> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Cmd msg) throws Exception {
        // TODO Auto-generated method stub
        // 处理传经ChannelPipeline的Cmd对象
    }
}
