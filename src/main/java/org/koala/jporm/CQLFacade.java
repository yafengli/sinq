package org.koala.jporm;

import org.koala.jporm.jpa.CQLCall;
import org.koala.jporm.jpa.JpaCall;
import org.koala.jporm.jpa.JpaFactory;
import org.koala.jporm.jpa.JpaService;

import javax.persistence.EntityManager;
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
public class CQLFacade extends JpaService {

    public CQLFacade(String persistenceName) {
        try {
            JpaFactory.bind(persistenceName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> T single(final Class<T> ct, final CQLCall<T> jqc) {
        return withEntityManager(new JpaCall<T>() {
            @Override
            public T call(EntityManager em) {
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<T> cq = cb.createQuery(ct);
                jqc.call(cb, cq);

                TypedQuery<T> query = em.createQuery(cq);
                return query.getSingleResult();
            }
        });
    }

    public <T> Long count(final Class<T> ct, final CQLCall<Long> jqc) {
        return withEntityManager(new JpaCall<Long>() {
            @Override
            public Long call(EntityManager em) {
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<Long> cq = cb.createQuery(Long.class);
                Root<T> root = cq.from(ct);
                cq.select(cb.count(root));
                jqc.call(cb, cq);

                TypedQuery<Long> query = em.createQuery(cq);
                return query.getSingleResult();
            }
        });
    }

    public <T> List<T> fetch(final Class<T> ct, final int limit, final int offset, final CQLCall<T> jqc) {
        return withEntityManager(new JpaCall<List<T>>() {
            @Override
            public List<T> call(EntityManager em) {
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<T> cq = cb.createQuery(ct);

                jqc.call(cb, cq);

                TypedQuery<T> query = em.createQuery(cq);
                if (offset > 0) query.setFirstResult(offset);
                if (limit > 0) query.setMaxResults(limit);
                return query.getResultList();
            }
        });
    }

    public <T> List<T> fetch(final Class<T> ct, final CQLCall<T> jqc) {
        return fetch(ct, -1, -1, jqc);
    }
}
