package io.sinq.expression

import scala.beans.BeanProperty
import scala.collection.mutable

trait ConditionII {

  import io.sinq.expression.ConditionII._

  @BeanProperty
  var from: ConditionII = _

  @BeanProperty
  val linkCache = new StringBuffer()

  @BeanProperty
  var flag: String = _
  @BeanProperty
  var root: ConditionII = _

  @BeanProperty
  var close = false

  lazy val to = mutable.ArrayBuffer[ConditionII]()

  def values: Seq[Any]

  def toField(): String

  def and(link: ConditionII): ConditionII = {
    if (this.getFrom == null) this.setRoot(this) else this.setRoot(this.getFrom.getRoot)
    link.setFlag(AND)
    nodeInit(link, this.getRoot)
    this.getRoot
  }

  def or(link: ConditionII): ConditionII = {
    if (this.getFrom == null) this.setRoot(this) else this.setRoot(this.getFrom.getRoot)
    link.setFlag(OR)
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

  private def endLoop(link: ConditionII, buffer: StringBuffer, params: mutable.ArrayBuffer[Any]): Unit = {
    if (link.getClose) buffer.append(START_BRACKET)
    if (link.getFrom != null) buffer.append(link.getFlag)
    link.values.foreach(params += _)
    buffer.append(link.toField())
    link.to.foreach(endLoop(_, buffer, params))
    if (link.getClose) buffer.append(END_BRACKET)
  }

  protected def nodeInit(link: ConditionII, root: ConditionII): Unit = {
    link.setFrom(this)
    nodeUp(link, root)
    nodeDown(link, root)

    this.to += link
  }

  protected def nodeUp(link: ConditionII, root: ConditionII): Unit = {
    link.setRoot(root)
    if (link.getFrom != null) nodeUp(link.getFrom, root)
  }

  protected def nodeDown(link: ConditionII, root: ConditionII): Unit = {
    link.setRoot(root)
    link.to.foreach(nodeDown(_, root))
  }
}

object ConditionII {
  val AND = " and "
  val OR = " or "
  val START_BRACKET = "("
  val END_BRACKET = ")"
}

trait Tuple1Condition extends ConditionII {
  def paramValue: Any

  override def values: Seq[Any] = Seq(paramValue)
}

trait Tuple2Condition extends ConditionII {
  def paramValue1: Any

  def paramValue2: Any

  override def values: Seq[Any] = Seq(paramValue1, paramValue2)
}