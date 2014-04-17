package demo.ii

import scala.collection.mutable.ListBuffer
import javax.persistence.criteria.Predicate

class CriteriaComposer[T](val from: Class[T]) {
  val _predicates = ListBuffer[Predicate]()
  val _where = ListBuffer[Predicate]()
}

object CriteriaComposer {
  def from[T](entityClass: Class[T]): CriteriaComposer[T] = {
    new CriteriaComposer[T](entityClass)
  }

  def join[V](joinClass: Class[V]): CriteriaComposer[V] = {
    new CriteriaComposer[V](joinClass)
  }
}

