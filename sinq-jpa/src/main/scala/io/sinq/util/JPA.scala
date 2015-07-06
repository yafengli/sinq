package io.sinq.util

import javax.persistence.{EntityManager, EntityManagerFactory, Persistence}

import org.slf4j.LoggerFactory

import scala.collection.concurrent.TrieMap

object JPA {
  val logger = LoggerFactory.getLogger(this.getClass)

  private val EMF_MAP = TrieMap[String, EntityManagerFactory]()

  /**
   * multi persistence name init.
   * @param pns persistence names
   */
  def initPersistenceName(pns: String*) {
    pns.takeWhile(!EMF_MAP.contains(_)).foreach(pn => EMF_MAP.put(pn, Persistence.createEntityManagerFactory(pn)))
  }

  /**
   * loop the current thread persistence name refer to EntityManagerFactory.
   * @param pn persistence name
   * @return Option EntityManagerFactory
   */
  def entityManagerFactory(pn: String): Option[EntityManagerFactory] = {
    if (logger.isErrorEnabled) {
      if (!EMF_MAP.contains(pn)) logger.error(s"#Not found persistence name:[${pn}] INIT.")
    }
    if (EMF_MAP.contains(pn)) Some(EMF_MAP(pn)) else None
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
