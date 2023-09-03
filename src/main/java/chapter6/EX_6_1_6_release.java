package chapter6;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

public class EX_6_1_6_release {
}
class DiscardInboundHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 通过调用 ReferenceCountUtil.release() 方法释放资源
        ReferenceCountUtil.release(msg);
    }
}

class SimpleDiscardInboundHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 通过调用 ReferenceCountUtil.release() 方法释放资源
        ReferenceCountUtil.release(msg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // channelRead0会自动释放资源
    }
}


class DiscardOutboundHandler extends ChannelOutboundHandlerAdapter {
    /**
     * 重要的是，不仅要释放资源，还要通知 ChannelPromise。否则可能会出现 Channel- FutureListener 收不到某个消息已经被处理了的通知的情况。
     * 总之，如果一个消息被消费或者丢弃了，并且没有传递给 ChannelPipeline 中的下一个 ChannelOutboundHandler，
     * 那么用户就有责任调用 ReferenceCountUtil.release()。 如果消息到达了实际的传输层，那么当它被写入时或者 Channel 关闭时，都将被自动释放。
     */
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ReferenceCountUtil.release(msg); // 释放资源

        promise.setSuccess(); // 通知ChannelPromise 数据已经被处理了
    }
}