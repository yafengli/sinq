package demo.ii

import javax.persistence.criteria._
import scala.collection.mutable.ListBuffer
import demo.ii.CriteriaOperator.CriteriaOperator
import java.util.Date

trait CriteriaProcessor[T] {
  def operator[V <: Comparable[V]](cb: CriteriaBuilder, root: Root[_], w: WhereContainer[V]): Predicate = {
    val v = w.vs
    val attr = root.get(w.attr)
    w.op match {
      case CriteriaOperator.BETWEEN => cb.between(attr, v(0), v(1))
      case CriteriaOperator.EQUAL => cb.equal(attr, v(0))
      case CriteriaOperator.GREATER_THAN => cb.greaterThan(attr, v(0))
      case CriteriaOperator.GREATER_THAN_EQUAL => cb.greaterThanOrEqualTo(attr, v(0))
      case CriteriaOperator.IN => attr.in(v)
      case CriteriaOperator.IS_NULL => cb.isNull(attr)
      case CriteriaOperator.LESS_THAN => cb.lessThan(attr, v(0))
      case CriteriaOperator.LESS_THAN_EQUAL => cb.lessThanOrEqualTo(attr, v(0))
      case CriteriaOperator.LIKE => cb.like(attr.asInstanceOf[Expression[String]], v(0).asInstanceOf[String])
      case _ => println(f"@@@@@@@${w.op.toString}"); null
    }
  }

  def generateWhere(cb: CriteriaBuilder, cq: CriteriaQuery[_], root: Root[_], cc: CriteriaComposer[T]): List[Predicate] = {
    val ps = ListBuffer[Predicate]()
    val and_s = ListBuffer[Predicate]()
    val or_s = ListBuffer[Predicate]()
    cc._where.foreach {
      w =>
        w.logicOperator match {
//          case LogicOperator.AND | LogicOperator.NONE => and_s += operator(cb, root, w)
          //          case LogicOperator.OR => or_s += operator(cb, root, w)
          case _ => println(f"#####${w.logicOperator}")
        }
    }
    if (and_s.size > 0) ps += cb.and(and_s: _*)
    if (or_s.size > 0) ps += cb.or(or_s: _*)
    ps.toList
  }

  def generateHaving(cb: CriteriaBuilder, cq: CriteriaQuery[_], root: Root[_], cc: CriteriaComposer[T]): List[Predicate] = {
    val ps = ListBuffer[Predicate]()
    val and_s = ListBuffer[Predicate]()
    val or_s = ListBuffer[Predicate]()
    cc._having.foreach {
      w =>
        w.logicOperator match {
          //case LogicOperator.AND | LogicOperator.NONE => and_s += operator(cb, root.get(w.attr), w.op, w.vs)
          //case LogicOperator.OR => or_s += operator(cb, root.get(w.attr), w.op, w.vs)
          case _ =>
        }
    }
    if (and_s.size > 0) ps += cb.and(and_s: _*)
    if (or_s.size > 0) ps += cb.or(or_s: _*)
    ps.toList
  }

  def generateSelect(cb: CriteriaBuilder, cq: CriteriaQuery[_], root: Root[_], cc: CriteriaComposer[T]): List[Selection[_]] = {
    cc._select.map {
      s =>
        s.aFun match {
          case AggregateFunction.SUM => cb.sum(root.get(s.name)).alias(s.alias)
          case AggregateFunction.AVG => cb.avg(root.get(s.name)).alias(s.alias)
          case AggregateFunction.COUNT => cb.count(root.get(s.name)).alias(s.alias)
          case AggregateFunction.MAX => cb.max(root.get(s.name)).alias(s.alias)
          case AggregateFunction.MIN => cb.min(root.get(s.name)).alias(s.alias)
        }
    }.toList
  }
}
