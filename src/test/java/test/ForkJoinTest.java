package test;

import models.jm.Author;
import org.h2.tools.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.koala.jporm.jpa.JpormFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

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
        JpormFacade orm = new JpormFacade("default");
        Author author = orm.single(Author.class, "t_find", params);
        if (author == null) {
            author = new Author();
            author.setName(name);
            orm.insert(author);
        }

        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 64; i++) {
            list.add(String.valueOf(i));
        }
        long start = System.currentTimeMillis();
        ForkJoinTask<?> task = new ConcurrentTask(list);
        ForkJoinPool pool = new ForkJoinPool();
        pool.submit(task);
        pool.shutdown();
        pool.awaitTermination(30, TimeUnit.SECONDS);
        long end = System.currentTimeMillis();

        logger.error("#time use " + (end - start) + "ms.");
    }
}

class ConcurrentTask extends RecursiveAction {
    private static final long serialVersionUID = -644147067302203957L;

    public static final Logger logger = LoggerFactory.getLogger(ConcurrentTask.class);

    private List<String> list = new ArrayList<String>();

    public static final Long count = 4L;

    ConcurrentTask(List<String> list) {
        this.list = list;
    }

    @Override
    protected void compute() {
        if (list.size() <= 1) {
            JpormFacade orm = new JpormFacade("default");
            logger.info("#list:" + list.toString() + " @" + orm);
            Map<String, Object> params = new HashMap<String, Object>() {
                {
                    put("name", "hello");
                }
            };
            Author author = orm.single(Author.class, "t_find", params);
            logger.info("#author:" + author);
        } else {
            invokeAll(new ConcurrentTask(list.subList(0, 1)),
                    new ConcurrentTask(list.subList(1, list.size())));
        }
    }
}