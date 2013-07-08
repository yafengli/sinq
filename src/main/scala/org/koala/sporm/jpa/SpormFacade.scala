package org.koala.sporm.jpa

import javax.persistence.Tuple
import javax.persistence.criteria.Selection
import javax.persistence.{EntityManager, Query}

class SpormFacade extends JPA with NQBuilder {

  import javax.persistence.criteria.{CriteriaQuery, Predicate}

  def insert[T](entity: T) {
    withTransaction {
      _.persist(entity)
    }
  }

  def update[T](entity: T) {
    withTransaction {
      _.merge(entity)
    }
  }

  def delete[T](entity: T) {
    withTransaction {
      _.remove(entity)
    }
  }

  def get[T](resultType: Class[T], id: Any): Option[T] = {
    withEntityManager {
      _.find(resultType, id)
    }
  }

  def fetch[T, X](fromType: Class[T], resultType: Class[X])(call: (CQExpression[T, T]) => Seq[Predicate]): Option[List[T]] = {
    withEntityManager {
      em =>
        CQBuilder(em, fromType, resultType).fetch(call)
    }
  }

  def fetch[T, X](fromType: Class[T], resultType: Class[X], limit: Int, offset: Int)(call: (CQExpression[T, T]) => Seq[Predicate]): Option[List[T]] = {
    withEntityManager {
      em =>
        CQBuilder(em, fromType, resultType).fetch(limit, offset)(call)
    }
  }

  def single[T](fromType: Class[T])(call: (CQExpression[T,T]) => Seq[Predicate]): Option[T] = {
    withEntityManager {
      em =>
        CQBuilder(em, fromType, fromType).single(call)
    }
  }

  def count[T](fromType: Class[T])(call: (CQExpression[T,Tuple]) => Seq[Predicate]): Option[Long] = {
    withEntityManager {
      em =>
        CQBuilder(em, fromType, classOf[java.lang.Long]).count(call)
    }
  }

  def multi[T](fromType: Class[T])(selectsCall: (CQExpression[T,Tuple]) => Seq[Selection[_]])(call: (CQExpression[T,Tuple]) => Seq[Predicate]): Option[List[Tuple]] = {
    withEntityManager {
      em =>
        CQBuilder(em, fromType, classOf[Tuple]).multi(selectsCall, call)
    }
  }

  def fetch[T](qs: String, pm: Map[String, Any], limit: Int, offset: Int)(f: (EntityManager) => Query): Option[List[T]] = {
    withEntityManager {
      em => _fetch(f(em), pm, limit, offset)
    }
  }

  def fetch[T](qs: String, pm: Map[String, Any])(f: (EntityManager) => Query): Option[List[T]] = {
    fetch(qs, pm, -1, -1)(f)
  }

  def single[T](qs: String, pm: Map[String, Any])(f: (EntityManager) => Query): Option[T] = {
    withEntityManager {
      em => _single(f(em), pm)
    }
  }

  def count(name: String, pm: Map[String, Any])(f: (EntityManager) => Query): Option[Long] = {
    withEntityManager {
      em => _count(f(em), pm)
    }
  }

  def count(name: String)(f: (EntityManager) => Query): Option[Long] = {
    count(name)(f)
  }

  def multi(name: String, pm: Map[String, Any])(f: (EntityManager) => Query): Option[List[_]] = {
    withEntityManager {
      em => _multi(f(em), pm)
    }
  }

  def multi[T](name: String)(f: (EntityManager) => Query): Option[List[_]] = {
    multi(name, Map[String, Any]())(f)
  }

  def sql[T](sql: String, params: Map[String, Any]): Option[List[T]] = {
    import scala.collection.JavaConversions._
    withEntityManager {
      em =>
        val query = em.createNativeQuery(sql)
        params.foreach {
          p =>
            query.setParameter(p._1, p._2)
        }
        query.getResultList.toList.asInstanceOf[List[T]]
    }
  }
}

object SpormFacade {
  private val facade_t = new ThreadLocal[SpormFacade]

  def apply(persistenceUnitName: String): SpormFacade = {
    JPA.initPersistenceName(persistenceUnitName)
    if (facade_t.get() == null) facade_t.set(new SpormFacade())
    facade_t.get()
  }
}