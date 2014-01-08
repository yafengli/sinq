package test;

import models.jm.Author;
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
    private static JpormFacade orm = null;

    @BeforeClass
    public static void init() {
        orm = new JpormFacade("default");
    }

    @Test
    public void testForkJoin() throws Exception {

        String name = "hello";
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("name", "hello");
        }};
        Author author = orm.single(Author.class, "t_find", params);
        if (author == null) {
            author = new Author();
            author.setName(name);
            orm.insert(author);
        }

        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 2; i++) {
            list.add(String.valueOf(i));
        }
        long start = System.currentTimeMillis();
        ForkJoinTask<?> task = new ConcurrentTask(list, orm);
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
    private JpormFacade orm;

    public static final Long count = 4L;

    ConcurrentTask(List<String> list, JpormFacade orm) {
        this.list = list;
        this.orm = orm;
    }

    @Override
    protected void compute() {
        if (list.size() <= 4) {
            logger.info("#list:" + list.toString() + " @" + orm);
            Map<String, Object> params = new HashMap<String, Object>() {
                {
                    put("name", "hello");
                }
            };
            Author author = orm.single(Author.class, "t_find", params);
            logger.info("#author:" + author);
        } else {
            invokeAll(new ConcurrentTask(list.subList(0, 4), orm),
                    new ConcurrentTask(list.subList(4, list.size()), orm));
        }
    }
}