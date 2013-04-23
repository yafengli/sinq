package org.koala.jporm.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: ya_feng_li@163.com
 * Date: 13-4-22
 * Time: 下午1:39
 */
public class JpaFactory {

    public static final Logger logger = LoggerFactory.getLogger(JpaFactory.class);
    public static final String P_U_KEY = "jpa.persistence.unit.name";
    private static final Map<String, EntityManagerFactory> emfMap = new ConcurrentHashMap<String, EntityManagerFactory>();
    private static final ThreadLocal<EntityManager> em_t = new InheritableThreadLocal<EntityManager>();
    private static final ThreadLocal<String> pn_t = new InheritableThreadLocal<String>();

    private JpaFactory() {
    }

    public static void bind(String pn) throws Exception {
        if (pn != null) {
            pn_t.set(pn);
        } else throw new Exception("#JPA PersistenceUnitName is NULL.");
    }

    public static void initPersistenceName(String pn) throws Exception {
        if (pn != null) {
            System.setProperty(P_U_KEY, pn);
        } else throw new Exception("#JPA PersistenceUnitName is NULL.");
    }

    public static EntityManagerFactory lookEntityManagerFactory() {
        String unitName = (pn_t.get() != null) ? pn_t.get() : System.getProperty(P_U_KEY);
        if (!emfMap.containsKey(unitName)) {
            emfMap.put(unitName, Persistence.createEntityManagerFactory(unitName));
        }
        return emfMap.get(unitName);
    }

    public static EntityManager createEntityManager() {
        if (em_t.get() == null) {
            try {
                EntityManager em = lookEntityManagerFactory().createEntityManager();
                em_t.set(em);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return em_t.get();
    }

    public static void close() {
        if (em_t.get() != null && em_t.get().isOpen()) {
            em_t.get().close();
            em_t.remove();
        }
    }

    public static void releaseAll() {
        for (Map.Entry<String, EntityManagerFactory> entry : emfMap.entrySet()) {
            entry.getValue().close();
        }
        emfMap.clear();
    }
}


