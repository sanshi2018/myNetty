package chapter11.delimited;

import io.netty.buffer.ByteBuf;
import lombok.Getter;

public class Cmd {
    @Getter
    private final ByteBuf name;
    @Getter
    private final ByteBuf args;
    public Cmd(ByteBuf name, ByteBuf args) {
        this.name = name;
        this.args = args;
    }

}
