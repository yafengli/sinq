package jpa.active

import javax.persistence._
import javax.persistence.criteria._
import scala.collection.JavaConversions._
import scala.collection.mutable.Buffer
import io.sinq.util.JPA 

trait Record[T] {
  def pn: String
  def getType: Class[_]

  def withEntityManager[E](call: (EntityManager) => E): E = {
    var em: EntityManager = null
    try {
      em = JPA.createEntityManager(pn)
      call(em)
    } finally {
      if (em != null) em.close
    }
  }

  def withTransaction[E](call: (EntityManager) => E): E = {
    var em: EntityManager = null
    try {
      em = JPA.createEntityManager(pn)
      em.getTransaction().begin()
      val r = call(em)
      em.getTransaction().commit()
      r
    } catch {
      case e: Exception =>
        if (em != null) em.getTransaction().rollback()
        e.printStackTrace()
        null.asInstanceOf[E]
    } finally {
      if (em != null) em.close
    }
  }

  def save(t: T): Unit = {
    withTransaction { _.persist(t) }
  }

  def update(t: T): Unit = {
    withTransaction { _.merge(t) }
  }

  def delete(t: T): Unit = {
    withTransaction { _.remove(t) }
  }

  def count(): Long = {
    withEntityManager { em =>
      val sql = s"select count(0) from ${getType.getName()}"
      println(s"sql:${sql}")
      val query = em.createQuery(sql)
      query.getSingleResult() match {
        case num: Number => num.longValue()
        case _ => 0L
      }
    }
  }

  def exists[K](id: K): Boolean = {
    withEntityManager { em =>
      em.find(getType, id) != null
    }
  }

  def find[K](id: K): T = {
    var t: T = null.asInstanceOf[T]
    withEntityManager { em =>
      t = em.find(getType.asInstanceOf[Class[T]], id)
    }
    t
  }
  /**
   * ocn:Order Column Name like "id"/"username"
   */
  def fetch[K](ocn: String, limit: Int = 0): Seq[T] = {
    val t = Buffer[T]()
    withEntityManager { em =>
      val tp = getType.asInstanceOf[Class[T]]
      val cb = em.getCriteriaBuilder()
      val q = cb.createQuery(tp)
      val b = q.from(tp)
      q.select(b).orderBy(cb.desc(b.get(ocn)))

      val ql = em.createQuery(q)
      val sql = ql.unwrap(classOf[org.hibernate.Query]).getQueryString()
      println(s"sql:${sql}")
      if (limit > 0) ql.setMaxResults(limit)
      ql.getResultList().foreach { e => t += e }
    }
    t.toSeq
  }

}
