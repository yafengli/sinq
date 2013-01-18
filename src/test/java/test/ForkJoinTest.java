package test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

public class ForkJoinTest {
    @Test
    public void testForkJoin() throws Exception {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 65536; i++) {
            list.add(String.valueOf(i));
        }
        long start = System.currentTimeMillis();
        ForkJoinTask<?> task = new ConcurrentTask(list);
        ForkJoinPool pool = new ForkJoinPool();
        pool.submit(task);
        pool.shutdown();
        pool.awaitTermination(30, TimeUnit.SECONDS);
        long end = System.currentTimeMillis();

        System.out.println("#time use " + (end - start) + "ms.");
    }
}


class ConcurrentTask extends RecursiveAction {
    private List<String> list = new ArrayList<>();

    public static final Long count = 4L;

    ConcurrentTask(List<String> list) {
        this.list = list;
    }

    @Override
    protected void compute() {
        if (list.size() <= 4) {
            System.out.println("#list:" + list.toString());
        } else {
            invokeAll(new ConcurrentTask(list.subList(0, 4)), new ConcurrentTask(list.subList(4, list.size())));
        }
    }
}