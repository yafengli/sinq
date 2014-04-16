package org.easy.test.criteria;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbHelper {
    public static final String[] allDataFiles = new String[]
            {
                    "sql/test_data.sql"
            };
    public static final String CONNECTION_URL = "jdbc:derby:memory:easy_test_db;create=true";
    private static EntityManager em;

    public static final String JDBC_DRIVER_CLASS = "org.apache.derby.jdbc.EmbeddedDriver";
    private static Logger log = LoggerFactory.getLogger(DbHelper.class);
    private static List<String> myDataFiles = new ArrayList<String>(0);
    public static final String PERSISTENCE_UNIT_NAME = "easy_test";

    private static boolean populated = false;

    public static void close() {
        em.close();

    }

    public static EntityManager getEntityManager() {
        return em;
    }

    public static int initDb(String connectionUrl, String... dataFiles) {

        Preconditions.checkNotNull(connectionUrl);
        Preconditions.checkNotNull(dataFiles);

        Preconditions.checkArgument(dataFiles.length > 0);

        Connection conn = null;
        int errorCount = 0;
        try {
            Class.forName(JDBC_DRIVER_CLASS);
            conn = DriverManager.getConnection(connectionUrl);
            Preconditions.checkNotNull(conn, "DB Connection is null");

            EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            em = factory.createEntityManager();

            for (String fileName : dataFiles) {
                if (!myDataFiles.contains(fileName)) {
                    InputStream fileInputStream = DbHelper.class.getClassLoader().getResourceAsStream(fileName);

                    Preconditions.checkNotNull(fileInputStream, "Unable to find " + fileName);
                    //errorCount += org.apache.derby.tools.ij.runScript(conn, fileInputStream, "UTF-8", System.out, "UTF-8");
                    myDataFiles.add(fileName);
                    log.debug("Processed " + dataFiles);
                } else {
                    log.debug("Skiped " + dataFiles);
                }
            }

            populated = true;

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            errorCount++;
//        } catch (UnsupportedEncodingException e) {
//            log.error(e.getMessage(), e);
//            errorCount++;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            errorCount++;
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                log.warn(e.getLocalizedMessage());
                errorCount++;
            }
        }

        return errorCount;
    }

    public static boolean isPopulatedWith(String... dataFiles) {
        if (populated) {
            for (int i = 0; i < dataFiles.length; i++) {
                if (!myDataFiles.contains(dataFiles[i]))
                    return false;
            }

            return true;
        } else
            return false;
    }

}
