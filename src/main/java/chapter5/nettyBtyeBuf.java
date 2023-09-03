package chapter5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;

public class nettyBtyeBuf {
    public ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(1024);

    public void heapBuf() {
        if (byteBuf.hasArray()) {
            byte[] array = byteBuf.array();
            int offset = byteBuf.arrayOffset() + byteBuf.readerIndex(); // 第一个字节的偏移量
            int length = byteBuf.readableBytes(); // 可读字节数
//            handleArray(array, offset, length);
        }
    }

    void directBuf() {
        if (!byteBuf.hasArray()) {
            int length = byteBuf.readableBytes();
            byte[] array = new byte[length]; // 分配一个新的数组来保存具有该长度的字节数据
            byteBuf.getBytes(byteBuf.readerIndex(), array); // 将字节复制到该数组
            //            handleArray(array, 0, length);
        }
    }

    void compositeBuf() {
        /**
         * 使用ByteBuffer 的复合缓冲区模式
         */
        ByteBuffer header = ByteBuffer.allocate(128);
        ByteBuffer body = ByteBuffer.allocate(1024);
        ByteBuffer[] message = new ByteBuffer[] { header, body }; // 使用一个数组来聚合这些消息

        // 创建一个新的ByteBuffer 并 聚合 header 和 body
        ByteBuffer message2 = ByteBuffer.allocate(header.remaining() + body.remaining());
        message2.put(header);
        message2.put(body);
        message2.flip();

        /**
         * 使用 CompositeByteBuf 的复合缓冲区模式
         */
        CompositeByteBuf messageBuf = Unpooled.compositeBuffer();
        ByteBuf headerBuf = Unpooled.directBuffer(); // 可以直接访问内存或者backing array
        ByteBuf bodyBuf = Unpooled.directBuffer(); // 可以直接访问内存或者backing array
        messageBuf.addComponents(headerBuf, bodyBuf); // 将 ByteBuf 实例追加到 CompositeByteBuf
        messageBuf.removeComponent(0); // 删除位于索引位置为 0（第一个组件）的 ByteBuf
        for (ByteBuf buf : messageBuf) {
            System.out.println(buf.toString());
        }

        /**
         * 访问 CompositeByteBuf 中的数据
         */
        CompositeByteBuf compBuf = Unpooled.compositeBuffer();
        int length = compBuf.readableBytes(); // 可读字节数
        byte[] array = new byte[length]; // 分配一个具有可读字节数长度的新数组
        compBuf.getBytes(compBuf.readerIndex(), array); // 将字节读到该数组中
        //        handleArray(array, 0, array.length); // 使用数组，偏移量和长度作为参数调用你的方法

    }
}
