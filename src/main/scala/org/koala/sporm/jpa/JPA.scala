package org.koala.sporm.jpa

import scala.collection.mutable
import javax.persistence.{EntityManagerFactory, EntityManager, Persistence}
import org.slf4j.LoggerFactory

trait JPA {

  import JPA._

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

  def lookEntityManagerFactory(): EntityManagerFactory = {
    val pn = PN_T.get()
    if (EMF_MAP.size == 1) EMF_MAP.toSeq.head._2
    else if (!EMF_MAP.contains(pn)) throw new Exception("#Not found persistence name BIND for current thread.")
    else EMF_MAP(pn)
  }

  def lookEntityManagerFactory(pn: String): EntityManagerFactory = {
    if (!EMF_MAP.contains(pn)) throw new Exception("#Not found persistence name INIT.")
    else EMF_MAP(pn)
  }

  def createEntityManager(): EntityManager = {
    lookEntityManagerFactory().createEntityManager()
  }

  def releaseAll() {
    EMF_MAP.foreach(_._2.close())
    EMF_MAP.clear()
  }
}

