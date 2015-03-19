package io.sinq.provider

import javax.persistence.EntityManager

import io.sinq.util.JPA

trait JPAProvider {

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
}



