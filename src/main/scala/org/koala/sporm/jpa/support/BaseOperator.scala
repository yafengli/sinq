package org.koala.sporm.jpa.support

import org.koala.sporm.jpa.JPA

/**
 * User: YaFengLi
 * Date: 12-12-12
 * Time: 下午4:36
 */
class BaseOperator[T](val entity: T) extends JPA {

  def insert() {
    withTransaction {
      //_.persist(entity)
      _.merge(entity)
    }
  }

  def update() {
    withTransaction {
      em =>
        em.merge(entity)
    }
  }

  def delete() {
    withTransaction {
      em =>
        em.remove(em.merge(entity))
    }
  }
}
