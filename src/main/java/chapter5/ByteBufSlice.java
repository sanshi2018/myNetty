package chapter5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * 派生缓冲区
 * 使用诸如slice() 和 duplicate() 方法来创建一个现有缓冲区的视图
 * 与原始缓冲区共享底层存储器 但拥有自己的读索引、写索引和标志位 与原始缓冲区相互独立   但共享数据
 * 对派生缓冲区的任何更新都将在原始缓冲区中可见
 */
public class ByteBufSlice {
    public static void main(String[] args) {

    }

    /**
     * 使用slice派生新数组
     */
    void slice() {
        Charset utf8 = Charset.forName("UTF-8");
        // 创建一个用于保存给定字符串的字节的 ByteBuf
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        ByteBuf sliced = buf.slice(0, 15); // 创建该 ByteBuf 从索引 0 开始到索引 15 结束的一个新切片
        System.out.println(sliced.toString(utf8)); // 将打印“Netty in Action”
        buf.setByte(0, (byte) 'J'); // 更新索引 0 处的字节
        System.out.println(sliced.toString(utf8)); // 将打印“Jetty in Action rocks!”
        assert buf.getByte(0) == sliced.getByte(0); // 将会成功，因为数据是共享的
    }

    /**
     * 使用copy派生新数组 ByteBuf 拥有独立的数据副本。
     */
    void copy() {
        Charset utf8 = Charset.forName("UTF-8");
        // 创建一个用于保存给定字符串的字节的 ByteBuf
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        ByteBuf copy = buf.copy(0, 15); // 创建该 ByteBuf 从索引 0 开始到索引 15 结束的分段的副本
        System.out.println(copy.toString(utf8)); // 将打印“Netty in Action”
        buf.setByte(0, (byte) 'J'); // 更新索引 0 处的字节
        System.out.println(copy.toString(utf8)); // 将打印“Jetty in Action rocks!”
        assert buf.getByte(0) != copy.getByte(0); // 将会成功，因为数据不是共享的
    }
}
