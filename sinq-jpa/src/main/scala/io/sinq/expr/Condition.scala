package io.sinq.expr

import io.sinq.{Table, Column}

import scala.beans.BeanProperty
import scala.collection.mutable

/**
 * 操作表达式
 */
trait Condition {

  import io.sinq.expr.Condition._

  //前一个条件表达式
  @BeanProperty
  var from: Condition = _
  //条件表达式连接字符
  @BeanProperty
  var flag: String = _
  //条件表达式根
  @BeanProperty
  var root: Condition = _
  //条件表达式是否封闭()
  @BeanProperty
  var close = false

  lazy val to = mutable.ArrayBuffer[Condition]()

  /**
   * @return 条件表达式字段
   */
  def column: Column[_]

  /**
   * @return 条件表达式参数集合
   */
  def values: List[Any]

  /**
   * @return 表达式字符串表达
   */
  def expression(tablesMap: Map[Table[_], String]): String

  /**
   * @param c 条件表达式
   * @return 条件表达式链表的根
   */
  def and(c: Condition): Condition = {
    link(c, AND)
  }

  /**
   * 使用and
   * @param c 条件表达式
   * @return 条件表达式链表的根
   */
  def or(c: Condition): Condition = {
    link(c, OR)
  }

  private def link(c: Condition, flag: String): Condition = {
    if (this.getFrom == null) this.setRoot(this) else this.setRoot(this.getFrom.getRoot)
    c.setFlag(flag)
    c.setClose(true)
    init(c, this.getRoot)
    this.getRoot
  }

  protected def init(c: Condition, root: Condition): Unit = {
    c.setFrom(this)
    lookUp(c, root)
    lookDown(c, root)

    this.to += c
  }

  protected def lookUp(c: Condition, root: Condition): Unit = {
    c.setRoot(root)
    if (c.getFrom != null) lookUp(c.getFrom, root)
  }

  protected def lookDown(c: Condition, root: Condition): Unit = {
    c.setRoot(root)
    c.to.foreach(lookDown(_, root))
  }
}

object Condition {
  val AND = " and "
  val OR = " or "
  val START_BRACKET = "("
  val END_BRACKET = ")"
}