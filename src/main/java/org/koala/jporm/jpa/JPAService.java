package org.koala.jporm.jpa;

import javax.persistence.EntityManager;

/**
 * User: ya_feng_li@163.com
 * Date: 13-4-22
 * Time: 下午1:32
 */
public abstract class JpaService {

    public <T> T withEntityManager(JpaCall<T> call) {
        T t = null;
        EntityManager em = JpaFactory.createEntityManager();
        try {
            t = call.call(em);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JpaFactory.close();
            return t;
        }
    }

    public <T> T withTransaction(JpaCall<T> call) {
        T t = null;
        EntityManager em = JpaFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            t = call.call(em);
            em.getTransaction().commit();

        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            JpaFactory.close();
            return t;
        }
    }
}
