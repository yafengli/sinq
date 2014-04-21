package demo.ii

import scala.collection.mutable.ListBuffer
import javax.persistence.criteria.JoinType
import demo.ii.CriteriaOperator.CriteriaOperator
import demo.ii.LogicOperator.LogicOperator
import demo.ii.LastCallType.LastCallType

final case class CriteriaComposer[T](val from: Class[T]) {
  val _select = ListBuffer[SelectContainer]()
  val _where = ListBuffer[WhereContainer]()
  val _join = ListBuffer[JoinContainer]()
  val _having = ListBuffer[HavingContainer]()
  val _order = ListBuffer[OrderContainer]()

  def and(): CriteriaComposer[T] = {

    WhereContainer[String]("name", CriteriaOperator.EQUAL, Nil)
    this
  }

  def or(): CriteriaComposer[T] = {
    this
  }
}

case class SelectContainer(val attr: String, val op: CriteriaOperator)

case class WhereContainer[V](val attr: String, val op: CriteriaOperator, val vs: List[V], val logicOp: LogicOperator = LogicOperator.AND, val lastCallType: LastCallType = LastCallType.WHERE)

case class JoinContainer(val attr: String, val joinType: JoinType)

case class HavingContainer(val attr: String, val op: CriteriaOperator)

case class GroupContainer(val attr: String, val op: CriteriaOperator)

case class OrderContainer(val attr: String, val op: CriteriaOperator)

object CriteriaComposer {
  def from[T](entityClass: Class[T]): CriteriaComposer[T] = {
    CriteriaComposer[T](entityClass)
  }

  def join[V](joinClass: Class[V]): CriteriaComposer[V] = {
    CriteriaComposer[V](joinClass)
  }
}

