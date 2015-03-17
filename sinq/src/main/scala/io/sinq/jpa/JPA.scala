package io.sinq.jpa

import javax.persistence.{EntityManager, EntityManagerFactory, Persistence}

import org.slf4j.LoggerFactory

import scala.collection.concurrent.TrieMap

trait JPA {

  import io.sinq.jpa.JPA._

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

  private val EMF_MAP = TrieMap[String, EntityManagerFactory]()
  private val PN_T = new ThreadLocal[String]

  val JPA_PERSISTENCE_DEFAULT_NAME = "sinq.jpa.persistence.name"

  /**
   * bind persistence name to current thread
   * @param pn persistence name
   */
  def bind(pn: String) {
    if (pn != null) {
      PN_T.set(pn)
      if (!EMF_MAP.contains(pn)) EMF_MAP += (pn -> Persistence.createEntityManagerFactory(pn))
    }
    else throw new Exception("#JPA PersistenceUnitName is NULL.")
  }

  /**
   * multi persistence name init.
   * @param pn persistence name
   */
  def initPersistenceName(pn: String*) {
    pn.foreach(n => EMF_MAP += (n -> Persistence.createEntityManagerFactory(n)))
    bind(pn(0))
    System.setProperty(JPA_PERSISTENCE_DEFAULT_NAME, pn(0))
  }

  /**
   * loop the current thread persistence name refer to EntityManagerFactory.
   * @return Option EntityManagerFactory
   */
  def entityManagerFactory(): Option[EntityManagerFactory] = {
    val pn = if (PN_T.get() != null) PN_T.get() else System.getProperty(JPA_PERSISTENCE_DEFAULT_NAME)
    if (EMF_MAP.contains(pn)) Some(EMF_MAP(pn))
    else {
      logger.error(s"##Not found persistence name:[${pn}].")
      None
    }
  }

  /**
   * loop the current thread persistence name refer to EntityManagerFactory.
   * @param pn persistence name
   * @return Option EntityManagerFactory
   */
  def entityManagerFactory(pn: String): Option[EntityManagerFactory] = {
    if (EMF_MAP.contains(pn)) Some(EMF_MAP(pn)) else None
  }

  /**
   * create EntityManager.
   * @return Option EntityManager
   */
  def createEntityManager(): EntityManager = {
    entityManagerFactory() match {
      case Some(emf) => emf.createEntityManager()
      case None => throw new Exception("#Not found persistence name INIT.")
    }
  }

  /**
   * create EntityManager by thread local persistence name.
   * @return Option EntityManager
   */
  def createEntityManager(pn: String): EntityManager = {
    entityManagerFactory(pn) match {
      case Some(emf) => emf.createEntityManager()
      case None => throw new Exception("#Not found persistence name INIT.")
    }
  }

  /**
   * release all EntityManagerFactory.
   */
  def release() {
    EMF_MAP.foreach(_._2.close())
    EMF_MAP.clear()
  }
}

