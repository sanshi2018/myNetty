package chapter9.exception;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.TooLongFrameException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class FrameChunkDecoderTest {
    @Test
    public void testFramesDecoded() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();

        EmbeddedChannel channel = new EmbeddedChannel(new FrameChunkDecoder(3));

        assertTrue(channel.writeInbound(input.readBytes(2))); // 将一个帧写入 ByteBuf
        try {
            channel.writeInbound(input.readBytes(4)); // 如果没有抛出异常，则会到达这个语句，并且测试失败
            Assert.fail(); // 如果没有抛出异常，则会到达这个语句，并且测试失败
        }catch (TooLongFrameException e) {
            // expected exception
            System.out.printf("TooLongFrameException: %s\n", e.getMessage());
        }

        assertTrue(channel.writeInbound(input.readBytes(3))); // 写入剩余的帧并断言产生一个有效的消息
        assertTrue(channel.finish()); // 将该 Channel 标记为已完成状态

        // Read frames
        ByteBuf read = (ByteBuf) channel.readInbound();
        Assert.assertEquals(buf.readSlice(2), read); // 读取产生的消息，并且验证值
        read.release();

        read = (ByteBuf) channel.readInbound();
        Assert.assertEquals(buf.skipBytes(4).readSlice(3), read); // 读取产生的消息，并且验证值
        read.release();
        buf.release();


    }
}
