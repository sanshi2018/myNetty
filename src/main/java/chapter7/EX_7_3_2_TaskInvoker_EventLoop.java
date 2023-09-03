package chapter7;

import io.netty.channel.Channel;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.concurrent.TimeUnit;

public class EX_7_3_2_TaskInvoker_EventLoop {
    void executor() {
        Channel channel = null;
        ScheduledFuture<?> future = channel.eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("60 seconds later");
            }
        }, 60, TimeUnit.SECONDS);
    }

    void executorPeriod() {
        Channel channel = null;
        ScheduledFuture<?> future = channel.eventLoop().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("60 seconds later");
            }
        }, 60, 60, TimeUnit.SECONDS);

        boolean mayInterruptIfRunning = true;
        future.cancel(mayInterruptIfRunning);
    }
}
