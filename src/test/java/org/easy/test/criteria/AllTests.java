package org.easy.test.criteria;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

@RunWith(Suite.class)
@Suite.SuiteClasses({CriteriaProcessorTest.class, BaseDaoTest.class})
public class AllTests {

    private static boolean initContextByMe = false;

    private static Logger log = LoggerFactory.getLogger(AllTests.class);

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        log.debug("setUpBeforeClass");

        // try loading Spring
        try {

            if (!DbHelper.isPopulatedWith(DbHelper.allDataFiles)) {
                int errorCount = DbHelper.initDb(DbHelper.CONNECTION_URL, DbHelper.allDataFiles);
                assertEquals(0, errorCount);
                initContextByMe = true;
            }

        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
    }

    public static Test suite() {

        TestSuite suite = new TestSuite(AllTests.class.getName());
        return suite;
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        if (initContextByMe)
            DbHelper.close();
    }

}
