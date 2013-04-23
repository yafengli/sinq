package org.koala.jporm.jpa;

import javax.persistence.EntityManager;

/**
 * User: ya_feng_li@163.com
 * Date: 13-4-22
 * Time: 下午1:34
 */
public interface JpaCall<T> {
    public T call(EntityManager em);
}
