package org.koala.sporm.jpa

import javax.persistence.criteria.Selection
import javax.persistence.{EntityManager, Query}


class SpormFacade extends JPA with NQBuilder {

  def insert[T](entity: T) {
    withTransaction {
      _.merge(entity)
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

  def get[T](rt: Class[T], id: Any): Option[T] = {
    withEntityManager {
      _.find(rt, id)
    }
  }

  def fetch[T,X](ft: Class[T])(call: (CQExpression[T,X]) => CQExpression[T,X]): Option[List[T]] = {
    withEntityManager {
      em => call(CQExpression(em, ft)).fetch()
    }
  }

  def fetch[T,X](ft: Class[T], limit: Int, offset: Int)(call: (CQExpression[T,X]) => CQExpression[T,X]): Option[List[T]] = {
    withEntityManager {
      em =>
        call(CQExpression(em, ft)).fetch(limit, offset)
    }
  }

  def single[T,X](ft: Class[T])(call: (CQExpression[T,X]) => CQExpression[T,X]): Option[T] = {
    withEntityManager {
      em => call(CQExpression(em, ft)).single()
    }
  }

  def count[T](ft: Class[T])(call: (CQExpression[T,java.lang.Long]) => CQExpression[T,java.lang.Long]): Option[Long] = {
    withEntityManager {
      em => call(CQExpression(em, ft, classOf[java.lang.Long])).count()
    }
  }

  def multi[T,X](ft: Class[T], selects: List[Selection[Any]])(call: (CQExpression[T,X]) => CQExpression[T,X]): Option[List[_]] = {
    withEntityManager {
      em => call(CQExpression(em, ft)).multi(selects)
    }
  }

  def inTransaction[T,X](ft: Class[T])(action: (CQExpression[T,X]) => CQExpression[T,X]) {
    withTransaction {
      em => action(CQExpression(em, ft))
    }
  }

  def inEntityManager[T,X](ft: Class[T])(action: (CQExpression[T,X]) => CQExpression[T,X]) {
    withEntityManager {
      em => action(CQExpression(em, ft))
    }
  }


  def fetch[T](qs: String, ops: Array[Any], limit: Int, offset: Int)(f: (EntityManager) => Query): Option[List[T]] = {
    withEntityManager {
      em => _fetch(f(em), ops, limit, offset)
    }
  }

  def fetch[T](qs: String, ops: Array[Any])(f: (EntityManager) => Query): Option[List[T]] = {
    fetch(qs, ops, -1, -1)(f)
  }

  def single[T](qs: String, ops: Array[Any])(f: (EntityManager) => Query): Option[T] = {
    withEntityManager {
      em => _single(f(em), ops)
    }
  }

  def count[T](name: String, ops: Array[Any])(f: (EntityManager) => Query): Option[Long] = {
    withEntityManager {
      em => _count(f(em), ops)
    }
  }

  def count[T](name: String)(f: (EntityManager) => Query): Option[Long] = {
    count(name)(f)
  }

  def multi[T](name: String, ops: Array[Any])(f: (EntityManager) => Query): Option[List[_]] = {
    withEntityManager {
      em => _multi(f(em), ops)
    }
  }

  def multi[T](name: String)(f: (EntityManager) => Query): Option[List[_]] = {
    multi(name, Array[Any]())(f)
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