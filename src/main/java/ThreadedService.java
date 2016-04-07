import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by zulk on 16.01.16.
 */
public class ThreadedService {


    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    boolean firstTime = true;

    public Long getSession() {
        synchronized (this) {
            System.out.println("sync");
            Future<Long> submit = executorService.submit(() -> {
                System.out.println("ENTER "+Thread.currentThread().getName());

                if (firstTime) {
                    System.out.println("First time");
                    firstTime = false;
                    Thread.sleep(30000000);
                    System.out.println("end");
//                throw new RuntimeException("dddd");
                }

                System.out.println(Thread.currentThread().getName());
                return Thread.currentThread().getId();
            });

            return go(submit);
        }
    }

    public Long go(Future<Long> future) {
        try {
            return future.get(4,TimeUnit.SECONDS);
        } catch (Exception e) {
            future.cancel(true);
//            executorService.shutdownNow();
//            recreateExecutor();
            throw new RuntimeException(e);
        }
    }

    private void recreateExecutor() {
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void stop() {
        executorService.shutdown();
    }
}
