package chapter11.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class HTTP_aggregator_initializer extends ChannelInitializer<Channel> {
    private final boolean client;

    public HTTP_aggregator_initializer(boolean client) {
        this.client = client;
    }
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (client) {
            pipeline.addLast("decoder", new HttpClientCodec());
        } else {
            pipeline.addLast("decoder", new HttpServerCodec());
        }
        // 将最大的消息大小为512 KB 的 HttpObjectAggregator 添加到 ChannelPipeline
        pipeline.addLast("aggregator", new HttpObjectAggregator(512 * 1024));
    }
}
