package chapter9.inboundMsg;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FixedLengthFrameDecoderTest {
    @Test
    public void testFramesDecoded() {
        ByteBuf buf = Unpooled.buffer(); // 创建一个 ByteBuf, 并存储 9 字节
        for (int i = 0; i <8; i++) {
            buf.writeByte(i);
        }

        ByteBuf input = buf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(
                new FixedLengthFrameDecoder(3));// 创建一个 EmbeddedChannel，并添加一个 FixedLengthFrameDecoder，其将以 3 字节的帧长度被测试
        // write bytes
        //失败的断言将导致一个异常被抛出，并将终止当前正在执行中的测试
        assertTrue(channel.writeInbound(input.retain())); // 将该 ByteBuf 传入到 decode() 方法
        assertTrue(channel.finish()); // 标记 Channel 为已完成状态

        // read messages
        ByteBuf read = (ByteBuf) channel.readInbound(); // 读取所生成的消息，并且验证是否有 3 帧（切片），其中每帧（切片）都为 3 字节
        assertTrue(buf.readSlice(3).equals(read));
        read.release();

        read = (ByteBuf) channel.readInbound();
        assertTrue(buf.readSlice(3).equals(read));
        read.release();

        read = (ByteBuf) channel.readInbound();
        assertTrue(buf.readSlice(3).equals(read));
        read.release();

        assertNull(channel.readInbound());
        buf.release();
    }

    @Test
    public void testFramesDecoded2() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }

        ByteBuf input = buf.duplicate();

        EmbeddedChannel channel = new EmbeddedChannel(
                new FixedLengthFrameDecoder(3));
        // 返回 false，因为没有一个完整的可供读取的帧
        assertFalse(channel.writeInbound(input.readBytes(2)));
        // 返回 true，因为至少存在一个完整的帧
        assertTrue(channel.writeInbound(input.readBytes(1)));
        assertTrue(channel.writeInbound(input.readBytes(6)));

        assertTrue(channel.finish());

        ByteBuf read = channel.readInbound();
        assertTrue(buf.readSlice(3).equals(read));
        read.release();

        read = channel.readInbound();
        assertTrue(buf.readSlice(3).equals(read));
        read.release();

        read = channel.readInbound();
        assertTrue(buf.readSlice(3).equals(read));
        read.release();

        assertNull(channel.readInbound());
        buf.release();
    }
}
