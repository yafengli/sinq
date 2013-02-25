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

  def fetch[T](ft: Class[T])(call: (CriteriaQL[T]) => CriteriaQL[T]): Option[List[T]] = {
    withEntityManager {
      em => call(CriteriaQL(em, ft)).fetch()
    }
  }

  def fetch[T](ft: Class[T], limit: Int, offset: Int)(call: (CriteriaQL[T]) => CriteriaQL[T]): Option[List[T]] = {
    withEntityManager {
      em =>
        call(CriteriaQL(em, ft)).fetch(limit, offset)
    }
  }

  def single[T](ft: Class[T])(call: (CriteriaQL[T]) => CriteriaQL[T]): Option[T] = {
    withEntityManager {
      em => call(CriteriaQL(em, ft)).single()
    }
  }

  def count[T](ft: Class[T])(call: (CriteriaQL[T]) => CriteriaQL[T]): Option[Long] = {
    withEntityManager {
      em => call(CriteriaQL(em, ft, classOf[java.lang.Long])).count()
    }
  }

  def multi[T](ft: Class[T], selects: List[Selection[Any]])(call: (CriteriaQL[T]) => CriteriaQL[T]): Option[List[_]] = {
    withEntityManager {
      em => call(CriteriaQL(em, ft)).multi(selects)
    }
  }

  def inTransaction[T](ft: Class[T])(action: (CriteriaQL[T]) => CriteriaQL[T]) {
    withTransaction {
      em => action(CriteriaQL(em, ft))
    }
  }

  def inEntityManager[T](ft: Class[T])(action: (CriteriaQL[T]) => CriteriaQL[T]) {
    withEntityManager {
      em => action(CriteriaQL(em, ft))
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