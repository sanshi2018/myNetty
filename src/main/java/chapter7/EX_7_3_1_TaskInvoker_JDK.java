package chapter7;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EX_7_3_1_TaskInvoker_JDK {

    public static void main(String[] args) throws InterruptedException {
        EX_7_3_1_TaskInvoker_JDK ex = new EX_7_3_1_TaskInvoker_JDK();
        System.out.println("-----------start-----------");
        ex.Executor();
    }

    /**
     *  使用ScheduledExecutorService来在 60 秒的延迟之后执行一个任务
     */
    void Executor() throws InterruptedException {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("5 seconds later");
            }
        }, 5, TimeUnit.SECONDS);
        // 打印executor的workers的hash
        System.out.println("-----------pro-----------");
        executor.awaitTermination(3, TimeUnit.SECONDS);
        // 立即关闭executor
        List<Runnable> runnables = executor.shutdownNow();
        // 等任务执行完毕后关闭executor
        executor.shutdown();
        System.out.println("-----------end-----------");

    }
}
