package org.koala.sporm.jpa


class SpormFacade extends JPA {

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

  def get[T](tc: Class[T], id: Any): Option[T] = {
    withEntityManager {
      _.find(tc, id)
    }
  }

  def fetch[T](ct: Class[T])(call: (CriteriaQL[T]) => CriteriaQL[T]): Option[List[T]] = {
    withEntityManager {
      em => call(CriteriaQL(em, ct)).fetch()
    }
  }

  def single[T](ct: Class[T])(call: (CriteriaQL[T]) => CriteriaQL[T]): Option[T] = {
    withEntityManager {
      em => call(CriteriaQL(em, ct)).single()
    }
  }

  def inTransaction[T](ct: Class[T])(action: (CriteriaQL[T]) => CriteriaQL[T]) {
    withTransaction {
      em => action(CriteriaQL(em, ct))
    }
  }

  def inEntityManager[T](ct: Class[T])(action: (CriteriaQL[T]) => CriteriaQL[T]) {
    withEntityManager {
      em => action(CriteriaQL(em, ct))
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