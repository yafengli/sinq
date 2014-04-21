package demo.ii

import scala.collection.mutable.ListBuffer
import javax.persistence.criteria.{Selection, JoinType}
import demo.ii.CriteriaOperator.CriteriaOperator
import demo.ii.AggregateFunction._
import demo.ii.NegationOperator.NegationOperator
import demo.ii.LogicOperator.LogicOperator
import javax.persistence.EntityManager

final class CriteriaComposer[T](val em: EntityManager, val from: Class[T]) extends CriteriaProcessor[T] {
  var lastCallType = LastCallType.WHERE
  val _select = ListBuffer[SelectContainer[_]]()
  val _where = ListBuffer[WhereContainer[_]]()
  val _join = ListBuffer[JoinContainer[_]]()
  val _having = ListBuffer[HavingContainer[_]]()
  val _order = ListBuffer[OrderContainer]()
  val multiSelect = ListBuffer[Selection[_]]()

  def where[V](attr: String, op: CriteriaOperator, v: V*): CriteriaComposer[T] = {
    _where += WhereContainer(attr, op, v)
    lastCallType = LastCallType.WHERE
    this
  }

  def and[V](attr: String, op: CriteriaOperator, v: V*): CriteriaComposer[T] = {
    lastCallType match {
      case LastCallType.WHERE =>
        if (_where.size > 0) _where.last.logicOperator = LogicOperator.AND
        _where += WhereContainer(attr, op, v)
      case LastCallType.HAVING =>
        if (_having.size > 0) _having.last.logicOperator = LogicOperator.AND
        _having += HavingContainer(attr, op, v)
    }
    this
  }

  def or[V](attr: String, op: CriteriaOperator, v: V*): CriteriaComposer[T] = {
    lastCallType match {
      case LastCallType.WHERE =>
        if (_where.size > 0) _where.last.logicOperator = LogicOperator.OR
        _where += WhereContainer(attr, op, v)
      case LastCallType.HAVING =>
        if (_having.size > 0) _having.last.logicOperator = LogicOperator.OR
        _having += HavingContainer(attr, op, v)
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

  def having[V](attr: String, op: CriteriaOperator, v: V*): CriteriaComposer[T] = {
    _having += HavingContainer(attr, op, v)
    lastCallType = LastCallType.HAVING
    this
  }

  def orderBy(): CriteriaComposer[T] = {
    this
  }


  def count(distinct: Boolean): Long = {
    val cb = em.getCriteriaBuilder
    val cq = cb.createQuery(classOf[Long])
    val root = cq.from(this.from)
    if (distinct) {
      cq.select(cb.countDistinct(root).asInstanceOf[Selection[Long]])
    } else {
      cq.select(cb.count(root).asInstanceOf[Selection[Long]])
    }
    cq.where(generateWhere(cb, cq, root, this): _*)
    cq.having(generateHaving(cb, cq, root, this): _*)
    em.createQuery(cq).getSingleResult
  }

  def single(): Option[T] = {
    try {
      val cb = em.getCriteriaBuilder
      val cq = cb.createQuery(this.from)
      val root = cq.from(this.from)
      cq.where(generateWhere(cb, cq, root, this): _*)
      cq.having(generateHaving(cb, cq, root, this): _*)
      Some(em.createQuery(cq).getSingleResult)
    } catch {
      case e: Exception =>
        None
    }
  }

  def singleTuple(): Option[T] = {
    try {
      val cb = em.getCriteriaBuilder
      val cq = cb.createQuery(this.from)
      val root = cq.from(this.from)
      cq.where(generateWhere(cb, cq, root, this): _*)
      cq.having(generateHaving(cb, cq, root, this): _*)
      cq.multiselect(generateSelect(cb, cq, root, this): _*)
      Some(em.createQuery(cq).getSingleResult)
    } catch {
      case e: Exception =>
        None
    }
  }
}

case class SelectContainer[V](val name: String, val alias: String, val aFun: AggregateFunction = null) {
  def this(name: String) = this(name, name, null)
}

case class WhereContainer[V](val attr: String, val op: CriteriaOperator, val vs: V*) {
  var logicOperator: LogicOperator = _
  var notOperator: NegationOperator = _
}

case class JoinContainer[V](val attr: String, val joinType: JoinType)

case class HavingContainer[V](val attr: String, val op: CriteriaOperator, val vs: V*) {
  var logicOperator: LogicOperator = LogicOperator.AND
  var notOperator: NegationOperator = null
}

case class GroupContainer(val attr: String, val op: CriteriaOperator)

case class OrderContainer(val attr: String, val op: CriteriaOperator)

object CriteriaComposer {
  def from[T](em: EntityManager, entityClass: Class[T]): CriteriaComposer[T] = {
    new CriteriaComposer[T](em, entityClass)
  }

  def join[V](em: EntityManager, joinClass: Class[V]): CriteriaComposer[V] = {
    new CriteriaComposer[V](em, joinClass)
  }
}

