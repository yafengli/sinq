package demo.ii

import scala.collection.mutable.ListBuffer
import javax.persistence.criteria.{Selection, JoinType}
import demo.ii.CriteriaOperator.CriteriaOperator
import demo.ii.AggregateFunction._
import demo.ii.NegationOperator.NegationOperator
import demo.ii.LogicOperator.LogicOperator

final class CriteriaComposer[T](val from: Class[T]) {
  var lastCallType = LastCallType.WHERE
  val _select = ListBuffer[SelectContainer[_]]()
  val _where = ListBuffer[WhereContainer[_]]()
  val _join = ListBuffer[JoinContainer[_]]()
  val _having = ListBuffer[HavingContainer]()
  val _order = ListBuffer[OrderContainer]()
  val multiSelect = ListBuffer[Selection[_]]()

  def where[V](attr: String, op: CriteriaOperator, v: V*): CriteriaComposer[T] = {
    _where += WhereContainer(attr, op, v)
    lastCallType = LastCallType.WHERE
    this
  }

  def and(): CriteriaComposer[T] = {
    lastCallType match {
      case LastCallType.WHERE =>
        _where.size match {
          case size: Int if size > 0 =>
            _where(size - 1).logicOperator = LogicOperator.AND
          case _ =>
        }
      case LastCallType.HAVING =>
        _having.size match {
          case size: Int if size > 0 =>
            _having(size - 1).logicOperator = LogicOperator.AND
          case _ =>
        }
    }

    this
  }

  def or(): CriteriaComposer[T] = {
    lastCallType match {
      case LastCallType.WHERE =>
        _where.size match {
          case size: Int if size > 0 =>
            _where(size - 1).logicOperator = LogicOperator.OR
          case _ =>
        }
      case LastCallType.HAVING =>
        _having.size match {
          case size: Int if size > 0 =>
            _having(size - 1).logicOperator = LogicOperator.OR
          case _ =>
        }
    }
    this
  }

  def select(): CriteriaComposer[T] = {
    this
  }

  def join(): CriteriaComposer[T] = {
    this
  }

  def groupBy(): CriteriaComposer[T] = {
    this
  }

  def having(): CriteriaComposer[T] = {
    this
  }

  def orderBy(): CriteriaComposer[T] = {
    this
  }
}

case class SelectContainer[V](val name: String, val alias: String = name, val aFun: AggregateFunction = null)

case class WhereContainer[V](val attr: String, val op: CriteriaOperator, val vs: V*) {
  var logicOperator: LogicOperator = _
  var notOperator: NegationOperator = _
}

case class JoinContainer[V](val attr: String, val joinType: JoinType)

case class HavingContainer(val attr: String, val op: CriteriaOperator, val vs: V*) {
  var logicOperator: LogicOperator = LogicOperator.AND
  var notOperator: NegationOperator = null
}

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

