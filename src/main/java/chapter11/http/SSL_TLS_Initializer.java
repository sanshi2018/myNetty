package chapter11.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

public class SSL_TLS_Initializer extends ChannelInitializer<Channel> {
    private final SslContext context;
    private final boolean startTls;

    public SSL_TLS_Initializer(SslContext context, boolean startTls) {
        this.context = context;
        this.startTls = startTls;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        SSLEngine sslEngine = context.newEngine(ch.alloc());
        ch.pipeline().addFirst("ssl", new SslHandler(sslEngine, startTls));
    }
}
