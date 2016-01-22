package io.sinq.provider.jpa

import javax.persistence.EntityManager

import io.sinq.util.JPA

trait JpaAdapter {

  def persistenceName: String

  def withTransaction[T](call: EntityManager => T): Option[T] = {
    val em = JPA.createEntityManager(persistenceName)
    try {
      em.getTransaction.begin()
      val t = call(em)
      em.getTransaction.commit()
      if (t == null || t == Nil) None else Some(t)
    } catch {
      case e: Exception =>
        e.printStackTrace()
        em.getTransaction.rollback()
        None
    }
    finally {
      em.close()
    }
  }

  def withEntityManager[T](call: EntityManager => T): Option[T] = {
    val em = JPA.createEntityManager(persistenceName)
    try {
      val t = call(em)
      if (t == null || t == Nil) None else Some(t)
    } catch {
      case e: Exception =>
        e.printStackTrace()
        None
    }
    finally {
      em.close()
    }
  }

  def find[T, K](id: K, t: Class[T]): Option[T] = {
    withEntityManager(_.find(t, id))
  }

  def insert[T](t: T): Unit = {
    withTransaction(_.persist(t))
  }

  def delete[T](t: T): Unit = {
    withTransaction(em => em.remove(em.merge(t)))
  }

  def update[T](t: T): Unit = {
    withTransaction(_.merge(t))
  }
}



