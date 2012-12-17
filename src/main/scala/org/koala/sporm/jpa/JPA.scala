package org.koala.sporm.jpa

import collection.concurrent.TrieMap
import javax.persistence.{EntityManagerFactory, EntityManager, Persistence}

trait JPA {

  import JPA._


  def createEntityManager(): EntityManager = {
    if (em_t.get() == null) {
      try {
        em_t.set(lookEntityManagerFactory().createEntityManager())
      } catch {
        case e: Exception => e.printStackTrace()
      }
    }
    em_t.get()
  }

  def close() {
    if (em_t.get() != null) {
      em_t.get().close()
      em_t.remove()
    }
  }

  def withTransaction[T](call: EntityManager => T): Option[T] = {
    val em = createEntityManager()
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
      close()
    }
  }

  def withEntityManager[T](call: EntityManager => T): Option[T] = {
    val em = createEntityManager()
    try {
      val t = call(em)
      if (t == null || t == Nil) None else Some(t)
    } catch {
      case e: Exception =>
        e.printStackTrace()
        None
    }
    finally {
      close()
    }
  }
}


object JPA {
  val P_U_KEY = "jpa.persistence.unit.name"
  private var emfMap = TrieMap[String, EntityManagerFactory]()
  private val em_t = new ThreadLocal[EntityManager]
  private val pn_t = new ThreadLocal[String]

  def bind(pn: String) {
    if (pn != null) {
      pn_t.set(pn)
    }
    else throw new Exception("#JPA PersistenceUnitName is NULL.")
  }

  def initPersistenceName(pn: String) {
    if (pn != null) {
      System.setProperty(P_U_KEY, pn)
    }
    else throw new Exception("#JPA PersistenceUnitName is NULL.")
  }

  def lookEntityManagerFactory(): EntityManagerFactory = {
    val unitName = if (pn_t.get() != null) pn_t.get() else System.getProperty(P_U_KEY)
    if (!emfMap.exists(p => p._1 == unitName))
      emfMap += (unitName -> Persistence.createEntityManagerFactory(unitName))

    emfMap(unitName)
  }

  def releaseAll() {
    emfMap.map(_._2.close())
    emfMap.clear()
  }
}

