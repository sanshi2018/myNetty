package chapter12.webSocket;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.logging.Logger;

public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String wsUri;
    private static byte[] FileBytes;
    private static File INDEX;
    private static final Logger logger = Logger.getLogger(HttpRequestHandler.class.getName());
    static {
        Class<?> aClass = HttpRequestHandler.class;
        InputStream resourceAsStream = aClass.getResourceAsStream("index.html");
        try {
            FileBytes = new byte[resourceAsStream.available()];
            // 将FileBytes写入到index.html
            INDEX = File.createTempFile("index", ".html");
            INDEX.deleteOnExit();
            RandomAccessFile file = new RandomAccessFile(INDEX, "rw");
            file.write(FileBytes);
            file.close();
        } catch (IOException e) {
            logger.info("Unable to locate index.html");
        }

    }

    public HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        logger.info("RECV------------------");
        // 如果请求了WebSocket协议升级，则增加引用计数（调用retain()方法），并将它传递给下一个ChannelInboundHandler
        if (wsUri.equalsIgnoreCase(req.uri())){
            ctx.fireChannelRead(req.retain());
        } else {
            if (HttpHeaders.is100ContinueExpected(req)){ // 处理100 Continue请求以符合HTTP 1.1规范
                send100Continue(ctx);
            }
            // 读取index.html
            RandomAccessFile file = new RandomAccessFile(INDEX, "r");
            HttpResponse response = new DefaultHttpResponse(
                    req.getProtocolVersion(), HttpResponseStatus.OK);
            response.headers().set(
                    HttpHeaders.Names.CONTENT_TYPE,
                    "text/plain; charset=UTF-8");
//            boolean keepAlive = HttpHeaders.isKeepAlive(req);
//            // 如果请求了keep-alive，则添加所需要的HTTP头信息
//            if (keepAlive){
//                response.headers().set(
//                        HttpHeaders.Names.CONTENT_LENGTH, file.length());
//                response.headers().set(
//                        HttpHeaders.Names.CONNECTION,
//                        HttpHeaders.Values.KEEP_ALIVE);
//            }
            // 将HttpResponse写到客户端
            ctx.write(response);
            // 将index.html写到客户端
            if (ctx.pipeline().get(SslHandler.class) == null){
                ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
            } else {
                ctx.write(new ChunkedNioFile(file.getChannel()));
            }
            // 写LastHttpContent并冲刷至客户端
            ChannelFuture future = ctx.writeAndFlush(
                    LastHttpContent.EMPTY_LAST_CONTENT);
            // 如果没有请求keep-alive，则在写操作完成后关闭Channel
//            if (!keepAlive){
//                future.addListener(f -> ctx.close());
//            }

        }
    }
    private static void send100Continue(ChannelHandlerContext ctx){
        HttpResponse response = new DefaultHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close(); // 关闭Channel并释放资源
    }
}
