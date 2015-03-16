package io.sinq.expression

import scala.beans.BeanProperty
import scala.collection.mutable

trait Condition {

  import io.sinq.expression.Condition._

  @BeanProperty
  var from: Condition = _

  @BeanProperty
  val linkCache = new StringBuffer()

  @BeanProperty
  var flag: String = _
  @BeanProperty
  var root: Condition = _

  @BeanProperty
  var close = false

  lazy val to = mutable.ArrayBuffer[Condition]()

  def values: Seq[Any]

  def toField(): String

  def and(link: Condition): Condition = {
    if (this.getFrom == null) this.setRoot(this) else this.setRoot(this.getFrom.getRoot)
    link.setFlag(AND)
    link.setClose(true)
    nodeInit(link, this.getRoot)
    this.getRoot
  }

  def or(link: Condition): Condition = {
    if (this.getFrom == null) this.setRoot(this) else this.setRoot(this.getFrom.getRoot)
    link.setFlag(OR)
    link.setClose(true)
    nodeInit(link, this.getRoot)
    this.getRoot
  }

  def translate(): String = {
    val buffer = new StringBuffer()
    val params = mutable.ArrayBuffer[Any]()
    endLoop(this, buffer, params)
    buffer.toString
  }

  def params(): Seq[Any] = {
    val buffer = new StringBuffer()
    val params = mutable.ArrayBuffer[Any]()
    endLoop(this, buffer, params)
    params.toSeq
  }

  private def endLoop(link: Condition, buffer: StringBuffer, params: mutable.ArrayBuffer[Any]): Unit = {
    if (link.getFrom != null) buffer.append(link.getFlag)
    if (link.getClose && link.to.size > 0) buffer.append(START_BRACKET)
    link.values.foreach(params += _)
    buffer.append(link.toField())
    link.to.foreach(endLoop(_, buffer, params))
    if (link.getClose && link.to.size > 0) buffer.append(END_BRACKET)
  }

  protected def nodeInit(link: Condition, root: Condition): Unit = {
    link.setFrom(this)
    nodeUp(link, root)
    nodeDown(link, root)

    this.to += link
  }

  protected def nodeUp(link: Condition, root: Condition): Unit = {
    link.setRoot(root)
    if (link.getFrom != null) nodeUp(link.getFrom, root)
  }

  protected def nodeDown(link: Condition, root: Condition): Unit = {
    link.setRoot(root)
    link.to.foreach(nodeDown(_, root))
  }
}

object Condition {
  val AND = " and "
  val OR = " or "
  val START_BRACKET = "("
  val END_BRACKET = ")"
}

trait Tuple1Condition extends Condition {
  def paramValue: Any

  override def values: Seq[Any] = Seq(paramValue)
}

trait Tuple2Condition extends Condition {
  def paramValue1: Any

  def paramValue2: Any

  override def values: Seq[Any] = Seq(paramValue1, paramValue2)
}