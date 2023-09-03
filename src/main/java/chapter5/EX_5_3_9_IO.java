package chapter5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

public class EX_5_3_9_IO {
    public static void main(String[] args) {
        EX_5_3_9_IO ex = new EX_5_3_9_IO();
//        ex.getSet();
        ex.readWrite();
    }
    void getSet() {
        Charset utf8 = Charset.forName("UTF-8");
        // 创建一个用于保存给定字符串的字节的 ByteBuf
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        // 打印第一个字符'N'
        System.out.println((char) buf.getByte(0));
        // 存储当前的 readerIndex 和 writerIndex
        int readerIndex = buf.readerIndex();
        int writerIndex = buf.writerIndex();
        // 将索引 0 处的字节更新为字符'B'
        buf.setByte(0, (byte) 'B');
        // 打印第一个字符，现在是'B'
        System.out.println((char) buf.getByte(0));
        // 将会成功，因为这些操作并不会修改相应的索引
        assert readerIndex == buf.readerIndex();
        assert writerIndex == buf.writerIndex();
    }

    void readWrite() {
        Charset utf8 = Charset.forName("UTF-8");
        // 创建一个用于保存给定字符串的字节的 ByteBuf
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        // 打印第一个字符'N'
        System.out.println((char) buf.readByte());
        // 存储当前的 readerIndex 和 writerIndex
        int readerIndex = buf.readerIndex();
        int writerIndex = buf.writerIndex();
        buf.writeByte((byte) '?'); // 写入一个'?'字符
        assert readerIndex == buf.readerIndex();
        assert writerIndex != buf.writerIndex(); // 将会成功，因为操作移动了写索引
    }
}
