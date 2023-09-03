package chapter6;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;

public class EX_6_2_2_ChannelPipeline extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 得到ChannelPipeline
        ChannelPipeline pipeline = ctx.pipeline();
        ChannelHandler channelHandler = pipeline.get("");

        pipeline.fireChannelRegistered();

        pipeline.bind(null);
    }
}
