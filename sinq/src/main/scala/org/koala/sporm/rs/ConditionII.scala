package org.koala.sporm.rs

import scala.beans.BeanProperty
import scala.collection.mutable

trait ConditionII {

  import org.koala.sporm.rs.ConditionII._

  @BeanProperty
  var from: ConditionII = _

  @BeanProperty
  val linkCache = new StringBuffer()

  @BeanProperty
  var flag: String = _
  @BeanProperty
  var root: ConditionII = _
  @BeanProperty
  var buffer = new StringBuffer()
  @BeanProperty
  var params = mutable.ArrayBuffer[Any]()

  val to = mutable.ArrayBuffer[ConditionII]()

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

  def toSql(): String = {
    if (this.getFrom != null) {
      this.setBuffer(this.getFrom.getBuffer)
      this.setParams(this.getFrom.getParams)
    }
    endLoop(this, this.getBuffer)
    this.getBuffer.toString
  }

  private def endLoop(link: ConditionII, buffer: StringBuffer): Unit = {
    if (link.getFrom != null) buffer.append(link.getFlag)
    if (link.getFrom != null && link.to.nonEmpty) {
      buffer.append(START_BRACKET)
      buffer.append(link.alias())
    }
    else buffer.append(link.alias())

    if (link.to.isEmpty) buffer.append(END_BRACKET)

    link.to.foreach(endLoop(_, buffer))
  }

  protected def store(values: Any*): Unit = {
    println(this.getRoot)
    values.foreach(this.getRoot.getParams += _)
  }

  def alias(): String

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

