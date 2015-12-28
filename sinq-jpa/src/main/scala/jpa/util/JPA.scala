package jpa.util

import javax.persistence._
import scala.collection.concurrent.TrieMap

object JPA {
  private val EMF_MAP = TrieMap[String, EntityManagerFactory]()
  def init(pn: String): Unit = {
    EMF_MAP += (pn -> Persistence.createEntityManagerFactory(pn))
  }

  def entityManager(pn: String): EntityManager = {
    EMF_MAP(pn).createEntityManager()
  }

  def release(): Unit = {
    EMF_MAP.foreach { e => e._2.close() }
    EMF_MAP.clear()
  }
}
