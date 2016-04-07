package luzu.test;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by zulk on 02.02.16.
 */
public class ThreadDaemonTest {


    public static void main(String[] args) {
        ThreadFactory damonThreadFactory = new ThreadFactoryBuilder().setDaemon(true).build();
        ExecutorService executorService = Executors.newSingleThreadExecutor(damonThreadFactory);
        executorService.submit(() -> infinite());
        executorService.shutdownNow();
        System.out.println("ended");
    }

    private static void infinite() {
        while(true) {
            ;
        }
    }
}
