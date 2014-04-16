package org.koala.jporm;

import org.koala.jporm.jpa.EntityService;
import org.koala.jporm.jpa.PersistenceFactory;

import javax.persistence.Query;
import java.util.List;
import java.util.Map;

/**
 * User: ya_feng_li@163.com
 * Date: 13-4-22
 * Time: 下午2:43
 */
public class SQLFacade extends EntityService {

    public SQLFacade(String persistenceName) {
        try {
            PersistenceFactory.bind(persistenceName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> T single(final Class<T> ct, final String queryName, final Map<String, Object> params) {
        return withEntityManager(em -> {
            Query query = em.createNamedQuery(queryName, ct);
            for (String key : params.keySet()) {
                query.setParameter(key, params.get(key));
            }
            return (T) query.getSingleResult();
        });
    }

    public Object[] single(final String queryName, final Map<String, Object> params) {
        return withEntityManager(em -> {
            Query query = em.createNamedQuery(queryName);

            for (String key : params.keySet()) {
                query.setParameter(key, params.get(key));
            }
            return (Object[]) query.getSingleResult();
        });
    }

    public Long count(final String queryName, final Map<String, Object> params) {
        return withEntityManager(em -> {
            Query query = em.createNamedQuery(queryName);
            for (String key : params.keySet()) {
                query.setParameter(key, params.get(key));
            }
            return ((Number) query.getSingleResult()).longValue();
        });
    }

    public <T> List<T> fetch(final Class<T> ct, final int limit, final int offset, final String queryName,
                             final Map<String, Object> params) {
        return withEntityManager(em -> {
            Query query = em.createNamedQuery(queryName, ct);
            if (offset > 0) query.setFirstResult(offset);
            if (limit > 0) query.setMaxResults(limit);

            for (String key : params.keySet()) {
                query.setParameter(key, params.get(key));
            }
            return query.getResultList();
        });
    }

    public <T> List<T> fetch(final Class<T> ct, final String queryName, final Map<String, Object> params) {
        return fetch(ct, -1, -1, queryName, params);
    }

    public List fetch(final int limit, final int offset, final String queryName,
                      final Map<String, Object> params) {
        return withEntityManager(em -> {
            Query query = em.createNamedQuery(queryName);
            if (offset > 0) query.setFirstResult(offset);
            if (limit > 0) query.setMaxResults(limit);
            for (String key : params.keySet()) {
                query.setParameter(key, params.get(key));
            }
            return query.getResultList();
        });
    }

    public List fetch(final String queryName, final Map<String, Object> params) {
        return fetch(-1, -1, queryName, params);
    }
}
