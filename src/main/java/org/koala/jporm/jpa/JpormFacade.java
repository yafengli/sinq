package org.koala.jporm.jpa;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * User: ya_feng_li@163.com
 * Date: 13-4-22
 * Time: 下午2:43
 */
public class JpormFacade extends JpaService {

    public JpormFacade(String persistenceName) {
        try {
            JpaFactory.initPersistenceName(persistenceName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> T single(final String queryName, final List<Object> params, final Class<T> ct) {
        return withEntityManager(new JpaCall<T>() {
            @Override
            public T call(EntityManager em) {
                Query query = em.createNamedQuery(queryName, ct);
                if (params != null) {
                    for (int i = 0; i < params.size(); i++) {
                        query.setParameter(i + 1, params.get(i));
                    }
                }
                return (T) query.getSingleResult();
            }
        });
    }

    public Object[] single(final String queryName, final List<Object> params) {
        return withEntityManager(new JpaCall<Object[]>() {
            @Override
            public Object[] call(EntityManager em) {
                Query query = em.createNamedQuery(queryName);

                if (params != null) {
                    for (int i = 0; i < params.size(); i++) {
                        query.setParameter(i + 1, params.get(i));
                    }
                }
                return (Object[]) query.getSingleResult();
            }
        });
    }


    public <T> List<T> fetch(final int limit, final int offset, final String queryName, final List<Object> params, final Class<T> ct) {
        return withEntityManager(new JpaCall<List<T>>() {
            @Override
            public List<T> call(EntityManager em) {
                Query query = em.createNamedQuery(queryName, ct);
                if (offset > 0) query.setFirstResult(offset);
                if (limit > 0) query.setMaxResults(limit);

                if (params != null) {
                    for (int i = 0; i < params.size(); i++) {
                        query.setParameter(i + 1, params.get(i));
                    }
                }
                return query.getResultList();
            }
        });
    }

    public <T> List<T> fetch(final String queryName, final List<Object> params, final Class<T> ct) {
        return fetch(-1, -1, queryName, params, ct);
    }

    public Long count(final String queryName, final List<Object> params) {
        return withEntityManager(new JpaCall<Long>() {
            @Override
            public Long call(EntityManager em) {
                Query query = em.createNamedQuery(queryName);
                if (params != null) {
                    for (int i = 0; i < params.size(); i++) {
                        query.setParameter(i + 1, params.get(i));
                    }
                }
                return ((Number) query.getSingleResult()).longValue();
            }
        });
    }

    public List multi(final int limit, final int offset, final String queryName, final List<Object> params) {
        return withEntityManager(new JpaCall<List>() {
            @Override
            public List call(EntityManager em) {
                Query query = em.createNamedQuery(queryName);
                if (offset > 0) query.setFirstResult(offset);
                if (limit > 0) query.setMaxResults(limit);
                if (params != null) {
                    for (int i = 0; i < params.size(); i++) {
                        query.setParameter(i + 1, params.get(i));
                    }
                }
                return query.getResultList();
            }
        });
    }

    public List multi(final String queryName, final List<Object> params) {
        return multi(-1, -1, queryName, params);
    }
}
