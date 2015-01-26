package org.koala.sporm.jpa

import javax.persistence.{EntityManager, EntityManagerFactory, Persistence}

import org.slf4j.LoggerFactory

import scala.collection.mutable

trait JPA {

  import org.koala.sporm.jpa.JPA._

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
      em.close()
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
      em.close()
    }
  }
}

object JPA {
  val logger = LoggerFactory.getLogger(classOf[JPA])

  private val EMF_MAP = mutable.HashMap[String, EntityManagerFactory]()
  private val PN_T = new ThreadLocal[String]

  /**
   * bind persistence name bind to current thread
   * @param pn
   */
  def bind(pn: String) {
    if (pn != null) {
      PN_T.set(pn)
    }
    else throw new Exception("#JPA PersistenceUnitName is NULL.")
  }

  /**
   * multi persistence name init
   * @param pn
   */
  def initPersistenceName(pn: String*) {
    pn.foreach {
      un =>
        EMF_MAP += (un -> Persistence.createEntityManagerFactory(un))
    }
    bind(pn(0))
  }

  def lookEntityManagerFactory(): Option[EntityManagerFactory] = {
    PN_T.get() match {
      case pn: String if EMF_MAP.contains(pn) => Some(EMF_MAP(pn))
      case _ => None
    }
  }

  def lookEntityManagerFactory(pn: String): Option[EntityManagerFactory] = {
    if (EMF_MAP.contains(pn)) Some(EMF_MAP(pn)) else None
  }

  def createEntityManager(): EntityManager = {
    lookEntityManagerFactory() match {
      case Some(emf) => emf.createEntityManager()
      case None => throw new Exception("#Not found persistence name INIT.")
    }
  }

  def releaseAll() {
    EMF_MAP.foreach(_._2.close())
    EMF_MAP.clear()
  }
}

