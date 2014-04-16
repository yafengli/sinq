package org.koala.jporm;

import org.koala.jporm.jpa.CriteriaQueryCall;
import org.koala.jporm.jpa.EntityService;
import org.koala.jporm.jpa.PersistenceFactory;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * User: ya_feng_li@163.com
 * Date: 13-4-22
 * Time: 下午2:43
 */
public class CQLFacade extends EntityService {

    public CQLFacade(String persistenceName) {
        try {
            PersistenceFactory.bind(persistenceName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> T single(final Class<T> ct, final CriteriaQueryCall<T> jqc) {
        return withEntityManager(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(ct);
            jqc.call(cb, cq);

            TypedQuery<T> query = em.createQuery(cq);
            return query.getSingleResult();
        });
    }

    public <T> Long count(final Class<T> ct, final CriteriaQueryCall<Long> jqc) {
        return withEntityManager(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<T> root = cq.from(ct);
            cq.select(cb.count(root));
            jqc.call(cb, cq);

            TypedQuery<Long> query = em.createQuery(cq);
            return query.getSingleResult();
        });
    }

    public <T> List<T> fetch(final Class<T> ct, final int limit, final int offset, final CriteriaQueryCall<T> jqc) {
        return withEntityManager(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(ct);

            jqc.call(cb, cq);

            TypedQuery<T> query = em.createQuery(cq);
            if (offset > 0) query.setFirstResult(offset);
            if (limit > 0) query.setMaxResults(limit);
            return query.getResultList();
        });
    }

    public <T> List<T> fetch(final Class<T> ct, final CriteriaQueryCall<T> jqc) {
        return fetch(ct, -1, -1, jqc);
    }
}
