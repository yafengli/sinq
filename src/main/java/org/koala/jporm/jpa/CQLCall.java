package org.koala.jporm.jpa;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

public interface CQLCall<T> {
    public void call(CriteriaBuilder cb, CriteriaQuery<T> cq);
}
