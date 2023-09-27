package chapter13.server;

import chapter13.LogEvent;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.URL;

public class LogEventBroadcaster {
    private final EventLoopGroup group;
    private final Bootstrap bootstrap;
    private final File file;

    public LogEventBroadcaster(InetSocketAddress address, File file) {
        this.group = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioDatagramChannel.class) // 引导该NioDatagramChannel（无连接的）
                .option(ChannelOption.SO_BROADCAST, true) // 设置SO_BROADCAST套接字选项
                .handler(new LogEventEncoder(address));
        this.file = file;
    }

    public void run() throws IOException, InterruptedException {
        Channel ch = bootstrap.bind(0).sync().channel();
        long pointer = 0;
        // 启动主处理循环
        for(;;) {
            long len = file.length();
            if (len < pointer) {
                // file was reset
                // 如果有必要，将文件指针设置到该文件的最后一个字节
                pointer = len;
            } else if (len > pointer) {
                // Content was added
                RandomAccessFile raf = new RandomAccessFile(file, "r");
                // 设置当前的文件指针，以确保没有任何的旧日志被发送
                raf.seek(pointer);
                String line;
                while ((line = raf.readLine()) != null) {
                    // 对于每个日志条目，写入一个LogEvent到Channel中
                    ch.writeAndFlush(new LogEvent(null, -1, file.getAbsolutePath(), line));
                }
                // 存储其在文件中的当前位置
                pointer = raf.getFilePointer();
                raf.close();
            }
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e) {
                Thread.interrupted();
                e.printStackTrace();
                break;
            }
        }
    }

    public void stop() {
        group.shutdownGracefully();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        File INDEX;
        Class<?> aClass = LogEventBroadcaster.class;
        String packagePath = aClass.getPackage().getName().replace('.', File.separatorChar);
        URL location = LogEventBroadcaster.class.getProtectionDomain().getCodeSource().getLocation();
        try {
            String path = location.toURI() +packagePath + "/text.txt";
            path = !path.contains("file:") ? path : path.substring(5);
            INDEX = new File(path);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to locate index.html", e);
        }
        LogEventBroadcaster broadcaster = new LogEventBroadcaster(
                new InetSocketAddress("255.255.255.255", Integer.parseInt("8080")), INDEX
        );
        try {
            broadcaster.run();
        }finally {
            broadcaster.stop();
        }
    }
}
