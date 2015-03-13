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
  var add = true

  lazy val params = mutable.ArrayBuffer[Any]()
  lazy val to = mutable.ArrayBuffer[ConditionII]()

  def values: Seq[Any]

  def alias(): String

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
    endLoop(this, buffer)
    buffer.toString
  }

  private def endLoop(link: ConditionII, buffer: StringBuffer): Unit = {
    if (link.getFrom != null) buffer.append(link.getFlag)
    if (link.getFrom != null && link.to.nonEmpty) {
      buffer.append(START_BRACKET)
      buffer.append(link.rule())
    }
    else buffer.append(link.rule())

    if (link.to.isEmpty) buffer.append(END_BRACKET)

    link.to.foreach(endLoop(_, buffer))
  }

  protected def rule(): String = {
    if (this.getAdd) {
      values.foreach(this.getRoot.params += _)
      this.setAdd(false)
    }
    alias()
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

