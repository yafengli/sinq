package test;

import models.Book;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

/**
 * User: YaFengLi
 * Date: 12-12-28
 * Time: 上午10:16
 */
public class JpaTest {
    @Test
    public void testCritiera() {
        EntityManager em = null;
        CriteriaBuilder qb = em.getCriteriaBuilder();

        CriteriaQuery<Long> cq = qb.createQuery(Long.class);

        cq.select(qb.count(cq.from(Book.class)));

        cq.where(/*your stuff*/);
    }
}

