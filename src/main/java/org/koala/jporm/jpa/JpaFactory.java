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
    public static final String PERSISTENCE_UNIT_NAME = "default";
    private static final Map<String, EntityManagerFactory> emfMap = new ConcurrentHashMap<String, EntityManagerFactory>();
    private static final ThreadLocal<String> pn_t = new InheritableThreadLocal<String>();

    private JpaFactory() {

    }

    public static void bind(String pn) {
        if (!emfMap.containsKey(pn)) {
            emfMap.put(pn, Persistence.createEntityManagerFactory(pn));
        }
        pn_t.set(pn);
    }

    public static EntityManagerFactory lookEntityManagerFactory() {
        String pn = (pn_t.get() != null) ? pn_t.get() : PERSISTENCE_UNIT_NAME;
        if (!emfMap.containsKey(pn)) {
            bind(pn);
        }
        return emfMap.get(pn);
    }

    public static EntityManager createEntityManager() {
        return lookEntityManagerFactory().createEntityManager();
    }

    public static void release() {
        for (String key : emfMap.keySet()) {
            emfMap.get(key).close();
        }
        emfMap.clear();
    }
}


