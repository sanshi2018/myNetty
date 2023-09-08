package chapter10.codec;

import io.netty.channel.CombinedChannelDuplexHandler;

public class EX_10_4_3_CombinedChannelDuplexHandler extends CombinedChannelDuplexHandler<ByteToCharDecoder, CharToByteEncoder> {
    public EX_10_4_3_CombinedChannelDuplexHandler() {
        super(new ByteToCharDecoder(), new CharToByteEncoder());
    }
}
