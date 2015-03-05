package org.koala.sporm.jpa

/**
 * User: YaFengLi
 * Date: 12-12-12
 * Time: 下午4:36
 */
class BaseBuilder[T](val entity: T) extends JPA {

  def insert(): Option[T] = {
    withTransaction(em => {
      em.persist(entity)
      entity
    })
  }

  def update(): Option[T] = {
    withTransaction(_.merge(entity))
  }

  def delete() {
    withTransaction {
      em =>
        em.remove(em.merge(entity))
    }
  }
}
