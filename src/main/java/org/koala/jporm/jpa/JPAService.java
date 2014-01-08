package org.koala.jporm.jpa;

import javax.persistence.EntityManager;

/**
 * User: ya_feng_li@163.com
 * Date: 13-4-22
 * Time: 下午1:32
 */
public abstract class JpaService {
    public <T> void insert(final T entity) {
        withTransaction(new JpaCall<T>() {
            @Override
            public T call(EntityManager em) {
                return em.merge(entity);
            }
        });
    }

    public <T> void update(final T entity) {
        withTransaction(new JpaCall<T>() {
            @Override
            public T call(EntityManager em) {
                return em.merge(entity);
            }
        });
    }

    public <T> void delete(final T entity) {
        withTransaction(new JpaCall<T>() {
            @Override
            public T call(EntityManager em) {
                em.remove(entity);
                return null;
            }
        });
    }

    public <T> T get(final Object id, final Class<T> ct) {
        return withEntityManager(new JpaCall<T>() {
            @Override
            public T call(EntityManager em) {
                return em.find(ct, id);
            }
        });
    }

    public Integer execute(final String queryName) {
        return withEntityManager(new JpaCall<Integer>() {
            @Override
            public Integer call(EntityManager em) {
                return em.createNativeQuery(queryName).executeUpdate();
            }
        });
    }

    public <T> T withEntityManager(JpaCall<T> call) {
        T t = null;
        EntityManager em = JpaFactory.createEntityManager();
        try {
            t = call.call(em);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
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
            em.close();
            return t;
        }
    }
}
