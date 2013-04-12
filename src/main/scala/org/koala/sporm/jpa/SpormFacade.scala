package org.koala.sporm.jpa

import javax.persistence.criteria.Selection


class SpormFacade extends JPA {

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

  def fetch[T](ft: Class[T])(call: (CQExpression[T]) => CQExpression[T]): Option[List[T]] = {
    withEntityManager {
      em => call(CQExpression(em, ft)).fetch()
    }
  }

  def fetch[T](ft: Class[T], limit: Int, offset: Int)(call: (CQExpression[T]) => CQExpression[T]): Option[List[T]] = {
    withEntityManager {
      em =>
        call(CQExpression(em, ft)).fetch(limit, offset)
    }
  }

  def single[T](ft: Class[T])(call: (CQExpression[T]) => CQExpression[T]): Option[T] = {
    withEntityManager {
      em => call(CQExpression(em, ft)).single()
    }
  }

  def count[T](ft: Class[T])(call: (CQExpression[T]) => CQExpression[T]): Option[Long] = {
    withEntityManager {
      em => call(CQExpression(em, ft, classOf[java.lang.Long])).count()
    }
  }

  def multi[T](ft: Class[T], selects: List[Selection[Any]])(call: (CQExpression[T]) => CQExpression[T]): Option[List[_]] = {
    withEntityManager {
      em => call(CQExpression(em, ft)).multi(selects)
    }
  }

  def inTransaction[T](ft: Class[T])(action: (CQExpression[T]) => CQExpression[T]) {
    withTransaction {
      em => action(CQExpression(em, ft))
    }
  }

  def inEntityManager[T](ft: Class[T])(action: (CQExpression[T]) => CQExpression[T]) {
    withEntityManager {
      em => action(CQExpression(em, ft))
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