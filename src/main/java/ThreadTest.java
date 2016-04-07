import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by zulk on 16.01.16.
 */
public class ThreadTest {


    static ThreadedService threadedService = new ThreadedService();

    public static void main(String[] args) throws InterruptedException {

//        ExecutorService executorService = Executors.newFixedThreadPool(1);

//        Callable<String> task = () -> {
////                Long session = threadedService.getSession();
////                return session.toString();
//            return Thread.currentThread().getName();
//        };

        execute(threadedService::getSession);
        execute(threadedService::getSession);
        execute(threadedService::getSession);
        execute(threadedService::getSession);
        threadedService.stop();
//        Collection<Callable<String>> t = new HashSet<>();
//        t.add(getTask());
//
//        t.add(getTask());
//
//        t.add(getTask());
//
//        List<Future<String>> futures = executorService.invokeAll(t);
//        futures.stream().forEach(s -> printFuture(s));
//        List<Future<String>> futures2 = executorService.invokeAll(t);
//        futures2.stream().forEach(s -> printFuture(s));
//
//        executorService.shutdown();
//        threadedService.stop();
    }

    private static void execute(Supplier f) {
        try {
            f.get();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    private static Callable<String> getTask() {
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                Long session = threadedService.getSession();
                return Thread.currentThread().getName()+session;
            }
        };
    }

    private static void printFuture(Future<String> s) {
        try {
            System.out.println(s.get());
        } catch (ExecutionException|InterruptedException e) {
            s.cancel(true);
            throw  new RuntimeException(e);
        }
    }
}
