package chapter5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

public class operateByte {
    ByteBuf src = PooledByteBufAllocator.DEFAULT.directBuffer(10);
    // 向 ByteBuf 中写入数据
    public void write(ByteBuf byteBuf, boolean isDirect) {
        for (int i = 0; i < byteBuf.capacity(); i++) {
            i = !isDirect? i : i*10;
            byteBuf.writeByte(i);
        }
    }

    public static void main(String[] args) {
        operateByte operateByte = new operateByte();
        operateByte.write(operateByte.src, false);
        ByteBuf dst = PooledByteBufAllocator.DEFAULT.directBuffer(5);
//        operateByte.write(dst,true);

        // 读取数据
        for (int i = 0; i < operateByte.src.readableBytes()/2; i++) {
            System.out.println(operateByte.src.readByte());
        }

        System.out.printf("------------------\n");
        // src --> dst
        operateByte.src.readBytes(dst);
        // dst --> src
        operateByte.src.writeBytes(dst);


//        for (int i = 0; i < dst.readableBytes(); i++) {
//            System.out.println(dst.getByte(i));
//        }


        // 写数据

    }
}
