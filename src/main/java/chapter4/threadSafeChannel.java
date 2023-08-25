package chapter4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.util.CharsetUtil;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class threadSafeChannel {
    /**
     * Netty 的 Channel 实现是线程安全的，因此你可以存储一个到 Channel 的引用，并且每当
     * 你需要向远程节点写数据时，都可以使用它，即使当时许多线程都在使用它
     * 消息将会被保证按顺序发送。
     */
    final Channel channel = null;
    final ByteBuf buf = Unpooled.copiedBuffer("your data", CharsetUtil.UTF_8).retain();

    void writeChannel() {
        Runnable writter = new Runnable() {
            @Override
            public void run() {
                channel.writeAndFlush(buf.duplicate());
            }
        };
        Executor executor = Executors.newCachedThreadPool();
        executor.execute(writter);
        executor.execute(writter);
    }
}
