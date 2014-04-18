package demo.ii

import scala.collection.mutable.ListBuffer
import javax.persistence.criteria.JoinType
import demo.ii.CriteriaOperator.CriteriaOperator

class CriteriaComposer[T](val from: Class[T]) {
  val _select = ListBuffer[SelectContainer]()
  val _where = ListBuffer[WhereContainer]()
  val _join = ListBuffer[JoinContainer]()
  val _having = ListBuffer[HavingContainer]()
  val _order = ListBuffer[OrderContainer]()

}

case class SelectContainer(val attr: String, val op: CriteriaOperator)

case class WhereContainer(val attr: String, val op: CriteriaOperator)

case class JoinContainer(val attr: String, val joinType: JoinType)

case class HavingContainer(val attr: String, val op: CriteriaOperator)

case class GroupContainer(val attr: String, val op: CriteriaOperator)

case class OrderContainer(val attr: String, val op: CriteriaOperator)

object CriteriaComposer {
  def from[T](entityClass: Class[T]): CriteriaComposer[T] = {
    new CriteriaComposer[T](entityClass)
  }

  def join[V](joinClass: Class[V]): CriteriaComposer[V] = {
    new CriteriaComposer[V](joinClass)
  }
}

