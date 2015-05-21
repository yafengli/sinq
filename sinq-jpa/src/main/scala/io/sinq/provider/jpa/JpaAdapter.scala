package io.sinq.provider.jpa

import java.sql.Connection
import javax.persistence.EntityManager

import io.sinq.provider.Provider
import io.sinq.util.JPA

trait JpaAdapter extends Provider {

  def persistenceName: String

  def withConnection[T](call: Connection => T): Option[T] = {
    val em = JPA.createEntityManager(persistenceName)
    try {
      val t = call(em.unwrap(classOf[Connection]))
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

  def withTransaction[T](call: EntityManager => T): Option[T] = {
    val em = JPA.createEntityManager(persistenceName)
    try {
      em.getTransaction.begin()
      val t = call(em)
      em.getTransaction.commit()
      if (t == null || t == Nil) None else Some(t)
    } catch {
      case e: Exception =>
        em.getTransaction.rollback()
        e.printStackTrace()
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



