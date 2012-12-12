package org.koala.sporm.jpa


class SpormFacade(val unitName: String) extends JPA {

  def this() = this(System.getProperty(JPA.P_U_KEY))

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
}

object SpormFacade {
  def apply(persistenceUnitName: String): SpormFacade = {
    new SpormFacade(persistenceUnitName)
  }

  def apply(): SpormFacade = {
    new SpormFacade()
  }
}