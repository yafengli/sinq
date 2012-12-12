package org.koala.sporm.jpa

/**
 * User: YaFengLi
 * Date: 12-12-12
 * Time: 下午4:36
 */
class BaseModelOpts[T](val entity: T) extends JPA {

  def insert() {
    withTransaction {
      _.persist(entity)
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
