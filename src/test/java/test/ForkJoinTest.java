package test;

import models.jm.Author;
import org.h2.tools.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.koala.jporm.jpa.CQLCall;
import org.koala.jporm.CQLFacade;
import org.koala.jporm.JPQLFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class ForkJoinTest {

    public static final Logger logger = LoggerFactory.getLogger(ForkJoinTest.class);

    public static Server server = null;

    @BeforeClass
    public static void initH2() throws Exception {
        server = Server.createTcpServer().start();
        logger.info("host:{} port:{}", server.getURL(), server.getPort());
    }

    @AfterClass
    public static void stop() {
        server.stop();
    }


    @Test
    public void testForkJoin() throws Exception {
        String name = "hello";
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("name", "hello");
        }};
        JPQLFacade orm = new JPQLFacade("default");
        Author author = orm.single(Author.class, "t_find", params);
        if (author == null) {
            author = new Author();
            author.setName(name);
            orm.insert(author);
        }

        long start = System.currentTimeMillis();
//        ForkJoinTask<?> task = new ConcurrentTask(5000);
        ForkJoinTask<Long> task = new WordCountTask(1, 1000);
        ForkJoinPool pool = new ForkJoinPool();
        pool.submit(task);

        pool.shutdown();
        pool.awaitTermination(30, TimeUnit.SECONDS);
        long end = System.currentTimeMillis();

        logger.error("#result:" + task.get() + "#time use " + (end - start) + "ms.");
    }
}

class ConcurrentTask extends RecursiveAction {
    private static final long serialVersionUID = -644147067302203957L;

    public static final Logger logger = LoggerFactory.getLogger(ConcurrentTask.class);

    public long count = 0L;
    public long times = 0L;

    ConcurrentTask(long count) {
        this.count = count;
    }


    private void jpql() {
        JPQLFacade orm = new JPQLFacade("default");
        logger.info("#count:" + count + " @" + orm);
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("name", "hello");
        }};
        Author author = orm.single(Author.class, "t_find", params);

    }

    private void cql() {
        CQLFacade orm = new CQLFacade("default");
        Long count = orm.count(Author.class, new CQLCall<Long>() {
            @Override
            public void call(CriteriaBuilder cb, final CriteriaQuery<Long> cq) {
                Root<Author> root = cq.from(Author.class);
                Path path = root.get("id");
                Predicate p1 = cb.ge(path, 12);
                cq.where(p1);
            }
        });
        orm.fetch(Author.class, new CQLCall<Author>() {
            @Override
            public void call(CriteriaBuilder cb, CriteriaQuery<Author> cq) {
                Root<Author> root = cq.from(Author.class);
                Predicate p1 = cb.equal(root.get("name"), "Hello");
                Predicate p2 = cb.ge((Path) root.get("id"), 1);
                cq.where(p1, p2);
            }
        });
    }

    @Override
    protected void compute() {
        if (count <= 1L) {
            //jpql();
            cql();
            times = count;

        } else {
            invokeAll(new ConcurrentTask(1L), new ConcurrentTask(count - 1L));
        }
    }
}

class WordCountTask extends RecursiveTask<Long> {
    private final long thread;
    private long size;

    WordCountTask(long thread, long size) {
        this.thread = thread;
        this.size = size;
    }

    @Override
    protected Long compute() {
        List<ForkJoinTask<Long>> forks = new LinkedList<>();
        if (size <= thread) {
            return size;
        } else {
            forks.add(new WordCountTask(thread, size - thread).fork());
        }
        for (ForkJoinTask<Long> task : forks) {
            size = size + task.join();
        }
        return size;
    }
}