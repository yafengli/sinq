package demo.ii

import javax.persistence.EntityManager
import javax.persistence.criteria._
import scala.collection.mutable.ListBuffer
import demo.ii.CriteriaOperator.CriteriaOperator
import scala.Some

class CriteriaProcessor(em: EntityManager) {

  import CriteriaProcessor._

  def count[T](cc: CriteriaComposer[T], distinct: Boolean): Long = {

    val cb = em.getCriteriaBuilder
    val cq = cb.createQuery(classOf[Long])
    val root = cq.from(cc.from)
    if (distinct) {
      cq.select(cb.countDistinct(root).asInstanceOf[Selection[Long]])
    } else {
      cq.select(cb.count(root).asInstanceOf[Selection[Long]])
    }
    cq.where(generateWhere(cb, cq, cc): _*)
    cq.having(generateHaving(cb, cq, cc): _*)
    em.createQuery(cq).getSingleResult
  }

  def single[T](cc: CriteriaComposer[T]): Option[T] = {
    try {
      val cb = em.getCriteriaBuilder
      val cq = cb.createQuery(cc.from)
      cq.where(generateWhere(cb, cq, cc): _*)
      cq.having(generateHaving(cb, cq, cc): _*)
      Some(em.createQuery(cq).getSingleResult)
    } catch {
      case e: Exception =>
        None
    }
  }

  def singleTuple[T](cc: CriteriaComposer[T]): Option[T] = {
    try {
      val cb = em.getCriteriaBuilder
      val cq = cb.createQuery(cc.from)
      cq.where(generateWhere(cb, cq, cc): _*)
      cq.having(generateHaving(cb, cq, cc): _*)
      cq.multiselect(generateSelect(cb, cq, cc): _*)
      Some(em.createQuery(cq).getSingleResult)
    } catch {
      case e: Exception =>
        None
    }
  }
}

object CriteriaProcessor {
  private def operator[V <: Comparable](cb: CriteriaBuilder, attr: Expression[_], op: CriteriaOperator, v: V*): Predicate = {
    op match {
      case CriteriaOperator.BETWEEN => cb.between(attr, v(0), v(1))
      case CriteriaOperator.EQUAL => cb.equal(attr, v(0))
      case CriteriaOperator.GREATER_THAN => cb.greaterThan(attr, v(0))
      case CriteriaOperator.GREATER_THAN_EQUAL => cb.greaterThanOrEqualTo(attr, v(0))
      case CriteriaOperator.IN => attr.in(v: _*)
      case CriteriaOperator.IS_NULL => cb.isNull(attr)
      case CriteriaOperator.LESS_THAN => cb.lessThan(attr, v(0))
      case CriteriaOperator.LESS_THAN_EQUAL => cb.lessThanOrEqualTo(attr, v(0))
      case CriteriaOperator.LIKE => cb.like(attr.asInstanceOf[Expression[String]], v(0).asInstanceOf[String])
    }
  }

  private def generateWhere[T](cb: CriteriaBuilder, cq: CriteriaQuery[T], cc: CriteriaComposer[T]): List[Predicate] = {
    val ps = ListBuffer[Predicate]()
    val and_s = ListBuffer[Predicate]()
    val or_s = ListBuffer[Predicate]()
    val root = cq.from(cc.from)
    cc._where.foreach {
      w =>
        w.logicOperator match {
          case LogicOperator.AND | LogicOperator.NONE => and_s += operator(cb, root.get(w.attr), w.op, w.vs: _*)
          case LogicOperator.OR => or_s += operator(cb, root.get(w.attr), w.op, w.vs: _*)
        }
    }
    ps += cb.and(and_s: _*)
    ps += cb.or(or_s: _*)
    ps.toList
  }

  private def generateHaving[T](cb: CriteriaBuilder, cq: CriteriaQuery[T], cc: CriteriaComposer[T]): List[Predicate] = {
    val ps = ListBuffer[Predicate]()
    val and_s = ListBuffer[Predicate]()
    val or_s = ListBuffer[Predicate]()
    val root = cq.from(cc.from)
    cc._having.foreach {
      w =>
        w.logicOperator match {
          case LogicOperator.AND | LogicOperator.NONE => and_s += operator(cb, root.get(w.attr), w.op, w.vs: _*)
          case LogicOperator.OR => or_s += operator(cb, root.get(w.attr), w.op, w.vs: _*)
        }
    }
    ps += cb.and(and_s: _*)
    ps += cb.or(or_s: _*)
    ps.toList
  }

  private def generateSelect[T](cb: CriteriaBuilder, cq: CriteriaQuery[T], cc: CriteriaComposer[T]): List[Selection[_]] = {
    val root = cq.from(cc.from)
    cc._select.map {
      s =>
        s.aFun match {
          case AggregateFunction.SUM => cb.sum(root.get(s.name)).alias(s.alias)
          case AggregateFunction.AVG => cb.avg(root.get(s.name)).alias(s.alias)
          case AggregateFunction.COUNT => cb.count(root.get(s.name)).alias(s.alias)
          case AggregateFunction.MAX => cb.max(root.get(s.name)).alias(s.alias)
          case AggregateFunction.MIN => cb.min(root.get(s.name)).alias(s.alias)
        }
    } toList
  }
}
