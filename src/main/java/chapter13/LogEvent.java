package chapter13;

import lombok.Getter;

import java.net.InetSocketAddress;

public class LogEvent {
    public static final byte SEPARATOR = (byte) ':';
    @Getter
    private final InetSocketAddress source;
    @Getter
    private final String logfile;
    @Getter
    private final String msg;
    @Getter
    private final long received;

    public LogEvent(String logfile, String msg) {
        this(null, -1, logfile, msg);
    }

    public LogEvent(InetSocketAddress source, long received, String logfile, String msg) {
        this.source = source;
        this.received = received;
        this.logfile = logfile;
        this.msg = msg;
    }
}
